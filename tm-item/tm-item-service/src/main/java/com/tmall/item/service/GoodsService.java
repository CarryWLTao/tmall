package com.tmall.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tmall.common.dto.CartDTO;
import com.tmall.common.enums.ExceptionEnum;
import com.tmall.common.exception.TmException;
import com.tmall.common.vo.PageResult;
import com.tmall.item.mapper.SkuMapper;
import com.tmall.item.mapper.SpuDetailMapper;
import com.tmall.item.mapper.SpuMapper;
import com.tmall.item.mapper.StockMapper;
import com.tmall.item.pojo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Copyright(C),2019-2019,CarryWLTao互联网工作室
 * FileName:SpuService
 * Author:  Administrator
 * Date:    2019-12-25 17:30
 * Description: 通用属性
 * Version:    1.0
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */
@Service
public class GoodsService {
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    public PageResult<Spu> querySpuByPage(Integer page, Integer rows, String saleable, String key) {
        //分页
        PageHelper.startPage(page, rows);
        //过滤
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        //搜索字段过滤
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title", "%" + key + "%");
        }
        //上下架过滤
        if (saleable != null) {
            criteria.andEqualTo("saleable", saleable);
        }
        //默认排序
        example.setOrderByClause("last_update_time DESC");
        //查询
        List<Spu> spus = spuMapper.selectByExample(example);

        //判断
        if (CollectionUtils.isEmpty(spus)) {
            throw new TmException(ExceptionEnum.GOODS_NOT_FOUND);
        }
        //解析分类和品牌名称
        loadCategoryAndBrandName(spus);
        //解析分页结果
        PageInfo<Spu> info = new PageInfo<>(spus);
        return new PageResult<>(info.getTotal(), spus);
    }

    private void loadCategoryAndBrandName(List<Spu> spus) {
        for (Spu spu : spus) {
            //处理分类名称
            List<String> names = categoryService.queryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()))
                    .stream().map(Category::getName).collect(Collectors.toList());
            spu.setCname(StringUtils.join(names, "/"));
            //处理品牌名称
            spu.setBname(brandService.queryById(spu.getBrandId()).getName());
        }
    }


    @Transactional
    public void saveGoods(Spu spu) {
        //新增SPU
        spu.setId(null);
        spu.setCreateTime(new Date());//创建时间
        spu.setLastUpdateTime(spu.getCreateTime());
        spu.setSaleable(true);
        spu.setValid(false);
        int count = spuMapper.insert(spu);
        if (count != 1) {
            throw new TmException(ExceptionEnum.GOODS_SAVE_ERROR);
        }
        //新增detail
        SpuDetail spuDetail = spu.getSpuDetail();
        spuDetail.setSpuId(spu.getId());
        spuDetailMapper.insert(spuDetail);
        //新增sku和库存
        saveSkuAndStock(spu);
        //发送mq消息
        amqpTemplate.convertAndSend("item.insert", spu.getId());

    }

    @Transactional
    public void saveSkuAndStock(Spu spu) {
        int count;
        //定义库存集合
        List<Stock> stockList = new ArrayList<>();
        //新增sku
        List<Sku> skus = spu.getSkus();
        for (Sku sku : skus) {
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            sku.setSpuId(spu.getId());
            count = skuMapper.insert(sku);
            if (count != 1) {
                throw new TmException(ExceptionEnum.GOODS_SAVE_ERROR);
            }
            //新增库存
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stockList.add(stock);
        }
        //批量新增库存
        count = stockMapper.insertList(stockList);
        if (count != stockList.size()) {
            throw new TmException(ExceptionEnum.GOODS_SAVE_ERROR);
        }
    }

    public SpuDetail queryDetailById(Long id) {
        SpuDetail spuDetail = spuDetailMapper.selectByPrimaryKey(id);
        if (spuDetail == null) {
            throw new TmException(ExceptionEnum.GOODS_DETAIL_NOT_FOUND);
        }
        return spuDetail;
    }

    public List<Sku> querySkuBySpuId(Long spuId) {
        //查询sku
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> skuList = skuMapper.select(sku);
        if (CollectionUtils.isEmpty(skuList)) {
            throw new TmException(ExceptionEnum.GOODS_SKU_NOT_FOUND);
        }
        //查询库存
     /*   for (Sku s: skuList ) {
            Stock stock = stockMapper.selectByPrimaryKey(s.getId());
            if (stock ==null){
                throw new TmException(ExceptionEnum.GOODS_STOCK_NOT_FOUND);
            }
            s.setStock(stock.getStock());
        }*/
        //查询库存
        List<Long> ids = skuList.stream().map(Sku::getId).collect(Collectors.toList());
        loadStockInSku(ids, skuList);
        return skuList;
    }

    @Transactional
    public void updateGoods(Spu spu) {
        /**
         * 更新策略：
         *      1.判断tb_spu_detail中的spec_template字段新旧是否一致
         *      2.如果一致说明修改的只是库存、价格和是否启用，那么就使用update
         *      3.如果不一致，说明修改了特有属性，那么需要把原来的sku全部删除，然后添加新的sku
         */
        if (spu.getId() == null) {
            throw new TmException(ExceptionEnum.GOODS_ID_CANNOT_NE_NULL);
        }
        Sku sku = new Sku();
        sku.setSpuId(spu.getId());
        //查询sku
        List<Sku> skuList = skuMapper.select(sku);
        if (!CollectionUtils.isEmpty(skuList)) {
            //删除sku
            skuMapper.delete(sku);
            //删除stock
            List<Long> ids = skuList.stream().map(Sku::getId).collect(Collectors.toList());
            stockMapper.deleteByIdList(ids);
        }
        //更新spu
        spu.setSaleable(null);
        spu.setValid(null);
        spu.setLastUpdateTime(new Date());
        spu.setCreateTime(null);

        int count = spuMapper.updateByPrimaryKeySelective(spu);
        if (count != 1) {
            throw new TmException(ExceptionEnum.GOODS_UPDATE_ERROR);
        }
        //修改detail
        count = spuDetailMapper.updateByPrimaryKeySelective(spu.getSpuDetail());
        if (count != 1) {
            throw new TmException(ExceptionEnum.GOODS_UPDATE_ERROR);
        }
        //新增sku和stock
        saveSkuAndStock(spu);

        //发送消息到mq
        amqpTemplate.convertAndSend("item.update", spu.getId());
    }


    public List<Sku> querySkuByIds(List<Long> ids) {
        List<Sku> skus = skuMapper.selectByIdList(ids);
        //如果skus为空则抛出异常
        if (CollectionUtils.isEmpty(skus)) {
            throw new TmException(ExceptionEnum.GOODS_SKU_NOT_FOUND);
        }
        loadStockInSku(ids, skus);
        return skus;
    }

    //查询库存
    private void loadStockInSku(List<Long> ids, List<Sku> skus) {
        List<Stock> stockList = stockMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(stockList)) {
            throw new TmException(ExceptionEnum.GOODS_STOCK_NOT_FOUND);
        }
        //我们把stock变成一个map,其key是:sku的id,值是库存值
        Map<Long, Integer> stockMap = stockList.stream().collect(Collectors.toMap(Stock::getSkuId, Stock::getStock));
        skus.forEach(s -> s.setStock(stockMap.get(s.getId())));
    }

    @Transactional
    public  void decreaseStock(List<CartDTO> carts) {
        for (CartDTO cart : carts) {
            //查询库存
            int count = stockMapper.decreaseStock(cart.getSkuId(), cart.getNum());
            if (count !=1){
                throw new TmException(ExceptionEnum.STOCK_NOT_ENOUGH);
            }

        }
    }
}

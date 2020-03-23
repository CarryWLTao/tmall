package com.tmall.search.repository;



import com.tmall.TmSearchApplication;
import com.tmall.common.vo.PageResult;
import com.tmall.item.pojo.Spu;
import com.tmall.search.client.GoodsClient;
import com.tmall.search.pojo.Goods;
import com.tmall.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TmSearchApplication.class)
public class GoodsRepositoryTest {
    @Autowired
    private  GoodsRepository goodsRepository;
    @Autowired
    private ElasticsearchTemplate template;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SearchService searchService;
    @Test
    public void testCreate() {
        //创建索引库
        template.createIndex(Goods.class);
        //映射关系
        template.putMapping(Goods.class);
    }
    @Test
    public void loadData(){
        int page = 1;
        int rows = 100;
        int size = 0;
        do {
            // 查询分页数据
            PageResult<Spu> result = goodsClient.querySpuByPage(page, rows, true, null);
            List<Spu> spuList = result.getItems();
            if(CollectionUtils.isEmpty(spuList)){
                break;
            }
            // 创建Goods集合
            List<Goods> goodsList = spuList.stream().map(searchService::buildGoods).collect(Collectors.toList());
            //存入索引库
            goodsRepository.saveAll(goodsList);
            //翻页
            page++;
            size=spuList.size();
        } while (size == 100);
    }

}

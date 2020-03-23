package com.tmall.item.api;

import com.tmall.common.dto.CartDTO;
import com.tmall.common.vo.PageResult;
import com.tmall.item.pojo.Sku;
import com.tmall.item.pojo.Spu;
import com.tmall.item.pojo.SpuDetail;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface GoodsApi {
    /**
     * 根据spu的id查询详情detail
     *
     * @param id
     * @return
     */
    @GetMapping("/spu/detail/{id}")
    SpuDetail queryDetailById(@PathVariable("id") Long id);

    /**
     * 临时用的
     *
     * @param id
     * @return
     */
    @GetMapping("/spu/{id}")
    Spu querySpuById(@PathVariable("id") Long id);

    /**
     * 根据spu查询下面的所有SKU
     *
     * @param spuId
     * @return
     */
    @GetMapping("sku/list")
    List<Sku> querySkuBySpuId(@RequestParam("id") Long spuId);

    /**
     * 分页查询SPU
     *
     * @param page
     * @param rows
     * @param saleable
     * @param key
     * @return
     */
    @GetMapping("/spu/page")
    PageResult<Spu> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "key", required = false) String key);

    /**
     * 根据id批量查询sku
     * @param ids
     * @return
     */
    @GetMapping("sku/list/ids")
    List<Sku> querySkuByIds(@RequestParam("ids") List<Long> ids);

    /**
     * 库存扣减
     * @param carts
     * @return
     */
    @PostMapping("stock/decrease")
    void decreaseStock(@RequestBody List<CartDTO> carts);

}

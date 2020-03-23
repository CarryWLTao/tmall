package com.tmall.search.client;


import com.tmall.item.api.CategoryApi;
import com.tmall.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;


@FeignClient("item-service")
public interface CategoryClient extends CategoryApi {

}

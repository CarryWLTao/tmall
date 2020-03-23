package com.tmall.search.client;

import com.tmall.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service")
public interface BrandClient  extends BrandApi {
}

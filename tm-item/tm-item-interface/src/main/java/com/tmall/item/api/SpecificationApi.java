package com.tmall.item.api;

import com.tmall.item.pojo.SpecParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SpecificationApi {
    /**
     * 根据组id查询参数
     * @param gid
     * @param cid
     * @param searching
     * @return
     */
    @GetMapping("spec/params")
    List<SpecParam> queryParamList(@RequestParam(value = "gid",required = false)Long gid,
                                                          @RequestParam(value = "cid",required = false)Long cid,
                                                          @RequestParam(value = "searching",required = false)Boolean searching);
}

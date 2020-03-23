package com.tmall.cart.service;/**
 * Copyright(C),2019-2020,CarryWLTao互联网工作室
 * FileName:CartService
 * Author:  Administrator
 * Date:    2020-03-12 15:53
 * Description: 购物车业务逻辑
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */

import com.tmall.auth.pojo.UserInfo;
import com.tmall.cart.interceptor.UserInterceptor;
import com.tmall.cart.pojo.Cart;
import com.tmall.common.enums.ExceptionEnum;
import com.tmall.common.exception.TmException;
import com.tmall.common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Copyright(C),2019-2020,CarryWLTao互联网工作室
 * FileName:CartService
 * Author:  Administrator
 * Date:    2020-03-12 15:53
 * Description: 购物车业务逻辑
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */
@Service
public class CartService {
    @Autowired
    private RedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "cart:uid";

    public void addCart(Cart cart) {
        //获取登录的用户
        UserInfo user = UserInterceptor.getUser();
        //key
        String key = KEY_PREFIX + user.getId();
        //hashkey 购物车商品的id
        String hashKey = cart.getSkuId().toString();
        //记录num
        Integer num = cart.getNum();
        //绑定key
        BoundHashOperations<String, Object, Object> operation = redisTemplate.boundHashOps(key);

        //判断当前购物车商品是否存在
        if (operation.hasKey(hashKey)) {
            //修改数量
            String json = operation.get(hashKey).toString();
            cart = JsonUtils.toBean(json, Cart.class);
            cart.setNum(cart.getNum() + num);
        }
        //写回redis
        operation.put(hashKey, JsonUtils.toString(cart));

    }

    public List<Cart> queryCartList() {
        //获取登录的用户
        UserInfo user = UserInterceptor.getUser();
        //key
        String key = KEY_PREFIX + user.getId();
        if (!redisTemplate.hasKey(key)) {
            //key不存在返回404
            throw new TmException(ExceptionEnum.CART_NOT_FOUND);
        }
        //获取登录用户的所有购物车
        BoundHashOperations<String, Object, Object> operation = redisTemplate.boundHashOps(key);
        List<Cart> carts = operation.values().stream().map(o -> JsonUtils.toBean(o.toString(), Cart.class))
                .collect(Collectors.toList());
        return carts;
    }

    public void updateCartNum(Long skuId, Integer num) {
        //获取登录的用户
        UserInfo user = UserInterceptor.getUser();
        //key
        String key = KEY_PREFIX + user.getId();
        //hashkey
        String hashKey = skuId.toString();
        //获取登录用户的所有购物车
        BoundHashOperations<String, Object, Object> operation = redisTemplate.boundHashOps(key);
        if (!operation.hasKey(hashKey)) {
            //key不存在返回404
            throw new TmException(ExceptionEnum.CART_NOT_FOUND);
        }
        Cart cart = JsonUtils.toBean(operation.get(hashKey).toString(), Cart.class);
        cart.setNum(num);
        //写回redis
        operation.put(hashKey,JsonUtils.toString(cart));
    }

    public void deleteCart(Long skuId) {
        //获取登录的用户
        UserInfo user = UserInterceptor.getUser();
        //key
        String key = KEY_PREFIX + user.getId();
        //删除
        redisTemplate.opsForHash().delete(key,skuId.toString());
    }
}

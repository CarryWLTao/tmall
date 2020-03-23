package com.tmall.cart.interceptor;

import com.tmall.auth.pojo.UserInfo;
import com.tmall.auth.utils.JwtUtils;
import com.tmall.cart.config.JwtProperties;
import com.tmall.common.utils.CookieUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 类说明
 * 拦截器
 */
@Slf4j
public class UserInterceptor implements HandlerInterceptor {
    private JwtProperties prop;
    private static final ThreadLocal<UserInfo> tl = new ThreadLocal<>();

    public UserInterceptor(JwtProperties prop) {
        this.prop = prop;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取cookie
        String token = CookieUtils.getCookieValue(request, prop.getCookieName());
        try {
            //解析token
            UserInfo user = JwtUtils.getInfoFromToken(token, prop.getPublicKey());
            //传递user
            //request.setAttribute("user", user);
            tl.set(user);
            //放行
            return true;
        } catch (Exception e) {
            log.error("[购物车服务]解析用户身份失败.", e);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //最后用完数据一定要清空
        tl.remove();
    }
    public static UserInfo getUser(){
        return tl.get();
    }
}

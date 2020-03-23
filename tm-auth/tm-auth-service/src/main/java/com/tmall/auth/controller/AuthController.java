package com.tmall.auth.controller;

import com.tmall.auth.config.JwtProperties;
import com.tmall.auth.pojo.UserInfo;
import com.tmall.auth.service.AuthService;
import com.tmall.auth.utils.JwtUtils;
import com.tmall.common.enums.ExceptionEnum;
import com.tmall.common.exception.TmException;
import com.tmall.common.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private JwtProperties prop;
    @Value("${tmall.jwt.cookieName}")
    private String cookieName;
    /**
     * 登录授权
     *
     * @param username
     * @param password
     * @return
     */
    @PostMapping("login")
    public ResponseEntity<Void> login(@RequestParam("username") String username,
                                      @RequestParam("password") String password,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {
        String token = authService.login(username, password);
        //TODO 写入cookie
      //  CookieUtils.newBuilder(response).httpOnly().request(request).build("TM_TOKEN",token);
        CookieUtils.setCookie(request,response,cookieName,token);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 校验用户登录状态
     * @return
     */
    @GetMapping("verify")
    public ResponseEntity<UserInfo> verify(@CookieValue("TM_TOKEN") String token,
                                           HttpServletRequest request,
                                           HttpServletResponse response){
       /* //判断token是否为空
         if (StringUtils.isBlank(token)) {
             //假如token为空 证明未登录返回403
            throw new TmException(ExceptionEnum.UNAUTHORIZED);
        }*/
         try{
             //解析token
             UserInfo info = JwtUtils.getInfoFromToken(token, prop.getPublicKey());
             //刷新token,重新生成token
             JwtUtils.generateToken(info,prop.getPrivateKey(),prop.getExpire());
             //TODO 写入cookie
             //  CookieUtils.newBuilder(response).httpOnly().request(request).build("TM_TOKEN",token);
             CookieUtils.setCookie(request,response,cookieName,token);
            //已登录则返回用信息
             return ResponseEntity.ok(info);
         }catch (Exception e){
             //token已过期,或者token无效被篡改
             throw new TmException(ExceptionEnum.UNAUTHORIZED);
         }

    }
}

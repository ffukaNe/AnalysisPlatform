package com.xia.springboot.common;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.xia.springboot.exception.ServiceException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



// 验证 token 拦截器
public class JwtInterceptor implements HandlerInterceptor {

    private static final String secretKey = "xia";

    // 重写拦截器
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 获取 token
        String token = request.getHeader("Authorization");
        // 验证 token 格式，如果为空，那么从url参数里面里面拿
        if (StrUtil.isBlank(token))
            token = request.getParameter("Authorization");
        // 判断该方法是否打上 @AuthAccess 注解，如果打上了，那么直接放行，如果这个 handler 是方法 handler
        if (handler instanceof HandlerMethod) {
            AuthAccess access = ((HandlerMethod) handler).getMethodAnnotation(AuthAccess.class);
            // 这里拿到注解我们可以在注解里面存放一些信息，然后通过 get 拿到
            if (access != null) {
                // 如果不为空，直接放行
                System.out.println(access.message());
                return true;
            }
        }
        // 执行认证
        // 如果还没有拿到 token 直接丢弃
        if (StrUtil.isBlank(token))
            throw new ServiceException("401","权限不够，请登录");
        // 获取 token 中的 userID
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException e) {
            throw new ServiceException("401","token错误");
        }
        /*
        // 根据 userId 查出用户
        User user = userMapper.selectById(userId);
        // 如果查不到
        if (user == null)
            throw new ServiceException("401","用户不存在");
         */
        // 通过用户密码生成验证器，因为我们使用的加密的 key 是用户密码
        // 如果我们不使用用户密码作为加密的 key ，那么这里其实可以不用查找用户，直接使用我们的 key 作为解密道具就可以了
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secretKey)).build();
        try {
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new ServiceException("401","token解析错误");
        }
        return true;
    }
}

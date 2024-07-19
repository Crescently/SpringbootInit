package com.cre.springbootinit.interceptor;


import com.cre.springbootinit.utils.JwtUtil;
import com.cre.springbootinit.utils.ThreadLocalUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
@Slf4j
// 全局登录拦截器
public class GlobalInterceptor implements HandlerInterceptor {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("Authorization");
        log.info("token:{}", token);
        // 验证token是否有效，如果无效则返回错误信息
        try {
            // 从redis中获取token
//            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
//            String redisToken = operations.get(token);
//            if (redisToken == null) {
//                throw new RuntimeException("token无效");
//            }
            // 解析token
            Map<String, Object> claims = JwtUtil.parseToken(token);
            // 把claims存入THREAD_LOCAL中，方便后续获取
            ThreadLocalUtil.set(claims);
            // 放行
            return true;
        } catch (Exception e) {
            log.info("请求失败");
            response.setStatus(401);
            // 不放行
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清空threadLocal中的数据 防止内存泄漏
        ThreadLocalUtil.remove();
    }
}

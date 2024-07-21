package com.cre.springbootinit.interceptor;


import com.cre.springbootinit.common.ErrorCode;
import com.cre.springbootinit.utils.JwtUtil;
import com.cre.springbootinit.utils.ThreadLocalUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
// 全局登录拦截器
public class GlobalInterceptor implements HandlerInterceptor {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("Authorization");
        // 验证token是否有效，如果无效则返回错误信息
        try {
            if (token == null || token.isEmpty()) {
                throw new RuntimeException("没有token，请确认是否登录");
            }

            // 从redis中获取token
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            String redisToken = operations.get(token);
            if (redisToken == null) {
                // token 已经失效了
                throw new RuntimeException("token失效了，请重新登录");
            }
            // 解析token
            Map<String, Object> claims = JwtUtil.parseToken(token);
            // 把claims存入THREAD_LOCAL中，方便后续获取
            ThreadLocalUtil.set(claims);
            // 放行
            return true;
        } catch (Exception e) {
            log.error("请求失败");
            returnErrorResponse(response, e.getMessage());
            // 不放行
            return false;
        }
    }

    private void returnErrorResponse(HttpServletResponse response, String message) {
        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");
        try {
            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("status", 401);
            errorDetails.put("code", ErrorCode.NOT_LOGIN_ERROR.getCode());
            errorDetails.put("message", message);
            ObjectMapper objectMapper = new ObjectMapper();
            response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
        } catch (IOException e) {
            log.error("Failed to write error response", e);
        }
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 清空threadLocal中的数据 防止内存泄漏
        ThreadLocalUtil.remove();
    }
}

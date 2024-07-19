package com.cre.springbootinit.config;

import com.cre.springbootinit.interceptor.GlobalInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 全局拦截器注册
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private GlobalInterceptor globalInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(globalInterceptor)
                // 排除登录和注册界面
                .excludePathPatterns("/user/login", "/user/register")
                // 排除swagger界面
                .excludePathPatterns("/v3/api-docs/**", "/swagger-ui.html/**","/swagger-ui/**");

    }


}

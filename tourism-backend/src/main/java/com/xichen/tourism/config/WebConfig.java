package com.xichen.tourism.config;

import com.xichen.tourism.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * springMvc web配置
 *
 * @author: ShanZhu
 * @date: 2024-08-10
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                //拦截所有请求
                .addPathPatterns("/**")
                //排除请求
                .excludePathPatterns("/file/**","/video/**","/img/**","/login","/common/**","/user/setUserAvatar/**"
                        ,"/user/getEmailReg/**","/user/forgetPassword/**","/user/saveUser/**");
    }

    // ========== 全局CORS跨域配置 ==========
    @Override
    public void addCorsMappings(org.springframework.web.servlet.config.annotation.CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3001", "http://127.0.0.1:3001")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
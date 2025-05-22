package com.ssafy.enjoytrip.config;

import com.ssafy.enjoytrip.interceptor.LogInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;


@Configuration
@RequiredArgsConstructor
public class MvcConfigure implements WebMvcConfigurer {
    private final LogInterceptor li;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	registry.addInterceptor(li);
    }

    @Value("${spring.servlet.multipart.location}")
    String filePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**").addResourceLocations("file:" + filePath + "/");
    }
}

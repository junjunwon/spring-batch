package com.springbatch.config;

import com.springbatch.common.interceptor.LoggerInterceptor;
import com.springbatch.common.interceptor.RateLimitInterceptor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@Profile("!test")
@Configuration
@ComponentScan
public class MvcConfig implements WebMvcConfigurer {

    private final LoggerInterceptor loggerInterceptor;
    private final RateLimitInterceptor rateLimitInterceptor;

    public MvcConfig(LoggerInterceptor loggerInterceptor,
                     RateLimitInterceptor rateLimitInterceptor) {
        this.loggerInterceptor = loggerInterceptor;
        this.rateLimitInterceptor = rateLimitInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggerInterceptor);
        registry.addInterceptor(rateLimitInterceptor);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));
    }
}

package com.wms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Cấu hình Spring Web MVC cho hệ thống SmartWMS.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Override
  public void addInterceptors(
    org.springframework.web.servlet.config.annotation.InterceptorRegistry registry
  ) {
    registry
      .addInterceptor(new SessionInterceptor())
      .addPathPatterns(
        "/app",
        "/index",
        "/index.html",
        "/dashboard",
        "/catalog/**",
        "/receipt/**",
        "/issue/**",
        "/inventory/**",
        "/auth/api/me"
      );
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    // Cấu hình phục vụ tài nguyên tĩnh linh hoạt: hỗ trợ cả root (/assets/...), (/static/...) và (/**)
    registry
      .addResourceHandler("/**")
      .addResourceLocations("classpath:/static/");
    registry
      .addResourceHandler("/static/**")
      .addResourceLocations("classpath:/static/");
    registry
      .addResourceHandler("/assets/**")
      .addResourceLocations("classpath:/static/assets/");
    registry
      .addResourceHandler("/css/**")
      .addResourceLocations("classpath:/static/css/");
  }
}

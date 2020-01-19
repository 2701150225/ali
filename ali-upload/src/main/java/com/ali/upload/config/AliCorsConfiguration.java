package com.ali.upload.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 使用CORS来解决ajax跨域问题
 *
 * @Author:wangsusheng
 * @Date: 2020/1/16 11:36
 **/
@Configuration
public class AliCorsConfiguration {

    @Bean
    public CorsFilter corsFilter() {
        //初始化cors配置对象
        CorsConfiguration configuration = new CorsConfiguration();
        //允许跨域的域名，如果要携带cookie，不能写*。*：代表所有域名都可以跨域访问
        configuration.addAllowedOrigin("http://manage.ali.com");
        configuration.setAllowCredentials(true); //是否允许携带cookie
        configuration.addAllowedMethod("*"); //*代表所有的请求方法，包括get post
        configuration.addAllowedHeader("*"); //设置头信息,*：允许携带任何头信息

        //初始化cors配置源对象
        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**", configuration);

        //返回corsFilter实例，参数：cors配置源对象
        return new CorsFilter(corsConfigurationSource);
    }
}

package com.xia.springboot.common;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @Author: Xia
 * @Date: 2024/4/2  16:06
 */

@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {

    // 自动注入 jwtInterceptor
    @Bean
    public JwtInterceptor jwtInterceptor(){
        return new JwtInterceptor();
    }


    // 加上拦截器
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        // 配置新的token登录拦截器和新的拦截规则，全部拦截
        registry.addInterceptor(jwtInterceptor()).addPathPatterns("/**").excludePathPatterns("/user/login","/files/**","/alipay/**");
        super.addInterceptors(registry);
    }

    //定义时间格式转换器
    @Bean
    public MappingJackson2HttpMessageConverter jackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm"));
        converter.setObjectMapper(mapper);
        return converter;
    }

    //添加转换器
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        //将我们定义的时间格式转换器添加到转换器列表中,
        //这样jackson格式化时候但凡遇到Date类型就会转换成我们定义的格式
        converters.add(jackson2HttpMessageConverter());
    }
}

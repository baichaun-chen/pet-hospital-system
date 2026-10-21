package com.huacai.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate 配置（Spring Boot 3.x 必需）
 * 解决：required a bean of type 'RestTemplate' that could not be found
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        // 设置超时（匹配 application.yml 中的配置）
        factory.setConnectTimeout(5000);  // 连接超时 5秒
        factory.setReadTimeout(15000);    // 读取超时 15秒
        return new RestTemplate(factory);
    }
}
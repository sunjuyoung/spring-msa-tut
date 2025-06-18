package com.example.ordersystem.common.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    @LoadBalanced  // 내부 서버간의 통신을 위한 로드밸런싱 //인터넷이 아니라 유레카서버 를 통해서 product-service에 요청을 보낸다.
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

package com.neo.springcloud.config;/**
 * @Author : neo
 * @Date 2021/11/1 11:29
 * @Description : TODO
 */

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @description:
 * @author: neo
 * @time: 2021/11/1 11:29
 */
@Configuration
public class ApplicationContextConfig {

    //applicationContext.xml  <bean id="" class="">

    @Bean
    @LoadBalanced       //是restTemplate负载均衡，默认是轮询机制
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}

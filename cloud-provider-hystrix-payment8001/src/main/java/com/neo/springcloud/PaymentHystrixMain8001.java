package com.neo.springcloud;/**
 * @Author : neo
 * @Date 2021/11/3 16:12
 * @Description : TODO
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @description: hystrix的主启动类
 * @author: neo
 * @time: 2021/11/3 16:12
 */

@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker   //启用服务降级的注解
public class PaymentHystrixMain8001 {
    public static void main(String[] args) {
        SpringApplication.run(PaymentHystrixMain8001.class, args);
    }
}

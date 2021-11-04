package com.neo.springcloud;/**
 * @Author : neo
 * @Date 2021/11/3 18:43
 * @Description : TODO
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @description:
 * @author: neo
 * @time: 2021/11/3 18:43
 */
@SpringBootApplication
@EnableFeignClients
@EnableEurekaClient
@EnableHystrix  //启用服务降级支持
public class OrderHystrixMain80 {
    public static void main(String[] args) {
        SpringApplication.run(OrderHystrixMain80.class, args);
    }
}

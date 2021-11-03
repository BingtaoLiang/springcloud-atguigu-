package com.neo.springcloud;/**
 * @Author : neo
 * @Date 2021/11/1 11:22
 * @Description : TODO
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @description:
 * @author: neo
 * @time: 2021/11/1 11:22
 */

@SpringBootApplication
@EnableEurekaClient
// @RibbonClient(name = "CLOUD-PAYMENT-SERVICE", configuration = MySelfRule.class)      //自定义轮训策略
public class OrderMain80 {
    public static void main(String[] args) {
        SpringApplication.run(OrderMain80.class, args);
    }
}

package com.neo.springcloud;/**
 * @Author : neo
 * @Date 2021/11/2 15:01
 * @Description : TODO
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @description:
 * @author: neo
 * @time: 2021/11/2 15:01
 */
@SpringBootApplication
@EnableDiscoveryClient          //该注解用于向使用consul或zookeeper作为注册中心时注册服务
public class PaymentMain8004 {
    public static void main(String[] args) {
        SpringApplication.run(PaymentMain8004.class, args);
    }
}

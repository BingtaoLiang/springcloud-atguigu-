package com.neo.springcloud;/**
 * @Author : neo
 * @Date 2021/11/4 16:11
 * @Description : TODO
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * @description:
 * @author: neo
 * @time: 2021/11/4 16:11
 */

@SpringBootApplication
@EnableHystrixDashboard     //启用dashboard
public class HystrixDashboardMain9001 {
    public static void main(String[] args) {
        SpringApplication.run(HystrixDashboardMain9001.class, args);
    }
}

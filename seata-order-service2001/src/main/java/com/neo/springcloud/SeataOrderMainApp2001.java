package com.neo.springcloud;/**
 * @Author : neo
 * @Date 2021/11/12 16:26
 * @Description : TODO
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @description:
 * @author: neo
 * @time: 2021/11/12 16:26
 */

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication(exclude= DataSourceAutoConfiguration.class)   //取消数据源的自动创建
public class SeataOrderMainApp2001 {
    public static void main(String[] args)
    {
        SpringApplication.run(SeataOrderMainApp2001.class, args);
    }
}

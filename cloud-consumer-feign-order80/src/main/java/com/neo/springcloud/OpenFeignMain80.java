package com.neo.springcloud;/**
 * @Author : neo
 * @Date 2021/11/3 10:11
 * @Description : TODO
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @description:
 * @author: neo
 * @time: 2021/11/3 10:11
 */

@SpringBootApplication
@EnableFeignClients
public class OpenFeignMain80 {
    public static void main(String[] args) {
        SpringApplication.run(OpenFeignMain80.class, args);
    }
}

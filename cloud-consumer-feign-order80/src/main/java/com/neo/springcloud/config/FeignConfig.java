package com.neo.springcloud.config;/**
 * @Author : neo
 * @Date 2021/11/3 15:24
 * @Description : TODO
 */

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @description:
 * @author: neo
 * @time: 2021/11/3 15:24
 */
@Configuration
public class FeignConfig {
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}

package com.neo.springcloud.config;/**
 * @Author : neo
 * @Date 2021/11/12 18:48
 * @Description : TODO
 */

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: neo
 * @time: 2021/11/12 18:48
 */

@Configuration
@MapperScan({"com.neo.springcloud.dao"})
public class MyBatisConfig {
}

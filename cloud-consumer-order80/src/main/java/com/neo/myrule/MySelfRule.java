package com.neo.myrule;/**
 * @Author : neo
 * @Date 2021/11/2 20:04
 * @Description : TODO
 */

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 自定义ribbon选择策略
 * @author: neo
 * @time: 2021/11/2 20:04
 */
@Configuration
public class MySelfRule {

    @Bean
    public IRule myRule() {
        return new RandomRule();       //自定义为随机策略
    }
}

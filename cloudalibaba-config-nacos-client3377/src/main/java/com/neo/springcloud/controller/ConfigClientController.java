package com.neo.springcloud.controller;/**
 * @Author : neo
 * @Date 2021/11/9 15:20
 * @Description : TODO
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: neobus
 * @time: 2021/11/9 15:20
 */

@RestController
@RefreshScope   //Nacos支持的动态刷新功能
public class ConfigClientController {
    @Value("${config.info}")
    private String configInfo;

    @GetMapping("/config/info")
    public String getConfigInfo() {
        return configInfo;
    }
}

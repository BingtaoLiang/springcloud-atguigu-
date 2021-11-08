package com.neo.springcloud.controller;/**
 * @Author : neo
 * @Date 2021/11/5 18:51
 * @Description : TODO
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: neo
 * @time: 2021/11/5 18:51
 */
@RestController
@RefreshScope       //动态刷新注解
public class ConfigClientController
{
    //这里的config.info代表的是github仓库中yml文件中的内容如下：
    //config:
    //   info: dev branch,springcloud-config/config-dev.yml version=8
    @Value("${config.info}")
    private String configInfo;

    @GetMapping("/configInfo")
    public String getConfigInfo()
    {
        return configInfo;
    }
}


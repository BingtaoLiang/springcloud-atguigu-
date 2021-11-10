package com.neo.springcloud.controller;/**
 * @Author : neo
 * @Date 2021/11/9 10:17
 * @Description : TODO
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: neo
 * @time: 2021/11/9 10:17
 */
@RestController
public class PaymentController {

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/payment/nacos/{id}")
    public String getPayment(@PathVariable("id") Integer id) {
        return "Nacos discovery,serverPort: " + serverPort + "\t id " + id;

    }

}

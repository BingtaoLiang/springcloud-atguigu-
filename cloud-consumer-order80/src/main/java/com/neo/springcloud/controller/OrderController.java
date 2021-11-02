package com.neo.springcloud.controller;/**
 * @Author : neo
 * @Date 2021/11/1 11:26
 * @Description : TODO
 */

import com.neo.springcloud.entities.CommonResult;
import com.neo.springcloud.entities.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @description:
 * @author: neo
 * @time: 2021/11/1 11:26
 */
@RestController
@Slf4j
public class OrderController {

    // public static final String PAYMENT_URL = "http://localhost:8001";        //单机版写法
    public static final String PAYMENT_URL = "http://CLOUD-PAYMENT-SERVICE";       //集群版写法

    @Autowired
    private RestTemplate restTemplate;


    @GetMapping("/consumer/payment/create")
    public CommonResult<Payment> create(Payment payment) {
        return restTemplate.postForObject(PAYMENT_URL + "/payment/create", payment, CommonResult.class);
    }

    @GetMapping("/consumer/payment/get/{id}")
    public CommonResult<Payment> getPayment(@PathVariable("id") Long id) {
        return restTemplate.getForObject(PAYMENT_URL + "/payment/get/" + id, CommonResult.class);
    }
}

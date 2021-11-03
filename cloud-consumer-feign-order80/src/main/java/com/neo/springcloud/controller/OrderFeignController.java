package com.neo.springcloud.controller;/**
 * @Author : neo
 * @Date 2021/11/3 10:17
 * @Description : TODO
 */

import com.neo.springcloud.entities.CommonResult;
import com.neo.springcloud.entities.Payment;
import com.neo.springcloud.service.PaymentFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @description:
 * @author: neo
 * @time: 2021/11/3 10:17
 */
@RestController
@Slf4j
public class OrderFeignController {
    @Resource
    private PaymentFeignService paymentFeignService;

    @GetMapping(value = "/consumer/payment/get/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id) {
        return paymentFeignService.getPaymentById(id);
    }

    @GetMapping("/consumer/payment/feign/timeout")
    public String paymentFeignTimeout() {
        //openfeign-ribbon ,客户端一般默认等待1秒钟
        return paymentFeignService.paymentFeignTimeout();
    }

}

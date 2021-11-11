package com.neo.springcloud.service;/**
 * @Author : neo
 * @Date 2021/11/11 16:31
 * @Description : TODO
 */

import com.neo.springcloud.entities.CommonResult;
import com.neo.springcloud.entities.Payment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @description:
 * @author: neo
 * @time: 2021/11/11 16:31
 */

@FeignClient(value = "nacos-payment-provider", fallback = PaymentFallbackService.class)
public interface PaymentService {

    @GetMapping(value = "/paymentSQL/{id}")
    public CommonResult<Payment> paymentSQL(@PathVariable("id") Long id);
}

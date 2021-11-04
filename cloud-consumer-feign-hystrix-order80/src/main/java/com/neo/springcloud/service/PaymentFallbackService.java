package com.neo.springcloud.service;/**
 * @Author : neo
 * @Date 2021/11/4 10:12
 * @Description : TODO
 */

import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: neo
 * @time: 2021/11/4 10:12
 */
@Component
public class PaymentFallbackService implements PaymentHystrixService {
    @Override
    public String paymentInfo_OK(Long id) {
        return "--PaymentFallbackService fallback -paymentInfo_OK,(╬￣皿￣)=○";
    }

    @Override
    public String paymentInfo_TimeOut(Long id) {
        return "--PaymentFallbackService fallback -paymentInfo_TimeOut,o(╥﹏╥)o";
    }
}

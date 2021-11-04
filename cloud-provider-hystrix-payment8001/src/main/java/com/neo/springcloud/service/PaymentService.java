package com.neo.springcloud.service;

import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author : neo
 * @Date 2021/11/3 16:15
 * @Description : TODO
 */
public interface PaymentService {

    //正常访问，肯定ok的方法
    public String paymentInfo_OK(Long id);

    //会超时的方法
    public String paymentInfo_TimeOut(Long id);

    //服务熔断
    public String paymentCircuitBreaker(@PathVariable("id") Integer id);
}

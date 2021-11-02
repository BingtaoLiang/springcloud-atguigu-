package com.neo.springcloud.service;/**
 * @Author : neo
 * @Date 2021/11/1 9:24
 * @Description : TODO
 */

import com.neo.springcloud.entities.Payment;

/**
 * @description:
 * @author: neo
 * @time: 2021/11/1 9:24
 */
public interface PaymentService {
    //写操作
    public int create(Payment payment);

    //读操作
    public Payment getPaymentById(Long id);

}

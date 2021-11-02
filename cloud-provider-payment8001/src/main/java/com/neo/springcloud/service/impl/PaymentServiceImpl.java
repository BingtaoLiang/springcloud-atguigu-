package com.neo.springcloud.service.impl;/**
 * @Author : neo
 * @Date 2021/11/1 9:26
 * @Description : TODO
 */

import com.neo.springcloud.dao.PaymentDao;
import com.neo.springcloud.entities.Payment;
import com.neo.springcloud.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: neo
 * @time: 2021/11/1 9:26
 */

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentDao paymentDao;

    //写操作
    public int create(Payment payment) {
        return paymentDao.create(payment);
    }

    //读操作
    public Payment getPaymentById(Long id) {
        return paymentDao.getPaymentById(id);
    }

}

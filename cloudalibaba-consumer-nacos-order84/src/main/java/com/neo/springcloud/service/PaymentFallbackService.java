package com.neo.springcloud.service;/**
 * @Author : neo
 * @Date 2021/11/11 16:35
 * @Description : TODO
 */

import com.neo.springcloud.entities.CommonResult;
import com.neo.springcloud.entities.Payment;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: neo
 * @time: 2021/11/11 16:35
 */

@Service
public class PaymentFallbackService implements PaymentService {
    @Override
    public CommonResult<Payment> paymentSQL(Long id) {
        return new CommonResult<>(444444, "服务降级返回，---PaymentFallbackService  " + new Payment(id, " errorSerial"));
    }
}

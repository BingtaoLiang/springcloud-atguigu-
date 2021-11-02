package com.neo.springcloud.dao;

import com.neo.springcloud.entities.Payment;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author : neo
 * @Date 2021/11/1 9:09
 * @Description : TODO
 */

@Mapper
public interface PaymentDao {


    //写操作
    public int create(Payment payment);

    //读操作
    public Payment getPaymentById(Long id);
}

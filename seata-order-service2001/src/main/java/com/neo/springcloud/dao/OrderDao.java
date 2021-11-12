package com.neo.springcloud.dao;/**
 * @Author : neo
 * @Date 2021/11/12 16:50
 * @Description : TODO
 */

import com.neo.springcloud.domain.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @description:
 * @author: neo
 * @time: 2021/11/12 16:50
 */
@Mapper
public interface OrderDao
{
    //1 新建订单
    void create(Order order);

    //2 修改订单状态，从零改为1
    void update(@Param("userId") Long userId, @Param("status") Integer status);
}

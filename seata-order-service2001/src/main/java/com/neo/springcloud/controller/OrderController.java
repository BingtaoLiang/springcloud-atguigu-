package com.neo.springcloud.controller;/**
 * @Author : neo
 * @Date 2021/11/12 17:16
 * @Description : TODO
 */

import com.neo.springcloud.domain.CommonResult;
import com.neo.springcloud.domain.Order;
import com.neo.springcloud.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: neo
 * @time: 2021/11/12 17:16
 */

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/order/create")
    public CommonResult create(Order order) {
        orderService.create(order);
        return new CommonResult(200,"订单创建成功");
    }


}

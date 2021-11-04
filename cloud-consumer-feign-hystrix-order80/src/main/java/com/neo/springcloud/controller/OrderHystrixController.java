package com.neo.springcloud.controller;/**
 * @Author : neo
 * @Date 2021/11/3 18:47
 * @Description : TODO
 */

import com.neo.springcloud.service.PaymentHystrixService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: neo
 * @time: 2021/11/3 18:47
 */
@RestController
@Slf4j
@DefaultProperties(defaultFallback = "payment_Global_FallbackMethod")   //设置全局兜底方法
public class OrderHystrixController {

    @Autowired
    private PaymentHystrixService paymentHystrixService;

    @HystrixCommand
    @GetMapping(value = "/consumer/payment/hystrix/ok/{id}")
    public String paymentInfo_OK(@PathVariable("id") Long id) {
        int age = 10 / 0;
        String result = paymentHystrixService.paymentInfo_OK(id);
        return result;
    }


    //@HystrixCommand注解指定了兜底的方法，以及划定触发兜底方法的阈值，这里设置服务响应时间3秒为阈值
    // @HystrixCommand(fallbackMethod = "paymentTimeOutFallbackMethod", commandProperties = {
    //         @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")
    // })
    // @HystrixCommand
    @GetMapping(value = "/consumer/payment/hystrix/timeout/{id}")
    public String paymentInfo_TimeOut(@PathVariable("id") Long id) {
        // int age = 10 / 0;
        String result = paymentHystrixService.paymentInfo_TimeOut(id);
        return result;
    }

    //兜底方法
    public String paymentTimeOutFallbackMethod(@PathVariable("id") Long id) {
        return "我是消费者80，对方支付系统繁忙或者自己运行出错，请10秒钟后再试，o(╥﹏╥)o";
    }


    //下面是全局fallback方法
    public String payment_Global_FallbackMethod() {
        return "Global异常处理信息，请稍后再试,(；´д｀)ゞ";
    }
}

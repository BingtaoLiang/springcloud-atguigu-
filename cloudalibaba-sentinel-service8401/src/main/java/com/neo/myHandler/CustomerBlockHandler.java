package com.neo.myHandler;/**
 * @Author : neo
 * @Date 2021/11/11 10:22
 * @Description : TODO
 */

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.neo.springcloud.entities.CommonResult;

/**
 * @description:
 * @author: neo
 * @time: 2021/11/11 10:22
 */
public class CustomerBlockHandler {
    public static CommonResult handlerException(BlockException exception)
    {
        return new CommonResult(4444,"按客戶自定义,global handlerException----1");
    }
    public static CommonResult handlerException2(BlockException exception)
    {
        return new CommonResult(4444,"按客戶自定义,global handlerException----2");
    }
}

package com.neo.springcloud.domain;/**
 * @Author : neo
 * @Date 2021/11/12 16:47
 * @Description : TODO
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: neo
 * @time: 2021/11/12 16:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult<T>
{
    private Integer code;
    private String  message;
    private T       data;

    public CommonResult(Integer code, String message)
    {
        this(code,message,null);
    }
}
package com.neo.springcloud.entities;/**
 * @Author : neo
 * @Date 2021/11/1 11:26
 * @Description : TODO
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: neo
 * @time: 2021/11/1 11:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult<T> {
    private Integer code;   //状态码200,404
    private String message; //提示信息
    private T date;     //返回的数据

    public CommonResult(Integer code, String message) {
        this(code, message, null);
    }
}

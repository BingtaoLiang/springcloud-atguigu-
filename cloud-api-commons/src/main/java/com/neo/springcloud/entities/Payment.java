package com.neo.springcloud.entities;/**
 * @Author : neo
 * @Date 2021/11/1 11:25
 * @Description : TODO
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description:
 * @author: neo
 * @time: 2021/11/1 11:25
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment implements Serializable {
    private Long id;
    private String serial;
}

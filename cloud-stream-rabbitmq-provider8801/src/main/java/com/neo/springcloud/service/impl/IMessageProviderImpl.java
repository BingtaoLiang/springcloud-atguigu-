package com.neo.springcloud.service.impl;/**
 * @Author : neo
 * @Date 2021/11/8 15:25
 * @Description : TODO
 */

import cn.hutool.core.util.IdUtil;
import com.neo.springcloud.service.IMessageProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;


/**
 * @description:
 * @author: neo
 * @time: 2021/11/8 15:25
 */

@EnableBinding(Source.class)    //定义消息的推送管道
public class IMessageProviderImpl implements IMessageProvider {

    @Autowired
    private MessageChannel output;

    @Override
    public String send() {
        String serial = IdUtil.randomUUID();
        output.send(MessageBuilder.withPayload(serial).build());
        System.out.println("********serial: " + serial);
        return null;
    }
}

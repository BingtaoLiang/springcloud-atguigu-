package com.neo.springcloud.controller;/**
 * @Author : neo
 * @Date 2021/11/8 16:02
 * @Description : TODO
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;

/**
 * @description:
 * @author: neo
 * @time: 2021/11/8 16:02
 */

@EnableBinding(Sink.class)
public class ReceiveMessageListenerController {
    @Value("${server.port}")
    private String serverPort;

    @StreamListener(Sink.INPUT)
    public void input(Message<String> message) {
        System.out.println("消费者1号，------>接收到的消息：" + message.getPayload() + "\t port:" + serverPort);
    }

}

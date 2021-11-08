package com.neo.springcloud.controller;/**
 * @Author : neo
 * @Date 2021/11/8 15:44
 * @Description : TODO
 */

import com.neo.springcloud.service.IMessageProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: neo
 * @time: 2021/11/8 15:44
 */
@RestController
public class SendMessageController {

    @Autowired
    private IMessageProvider messageProvider;


    @GetMapping("/sendMessage")
    public String sendMessage() {
        return messageProvider.send();
    }
}

package com.example.demo.controller;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RabbitSender {

//自动注入RabbitTemplate模板类

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendmsg")
    public String send(@RequestParam("s") String s)throws Exception {
        //String s ="123456";
        //id + 时间戳 全局唯一
        CorrelationData correlationData =new CorrelationData("1234567890");

        rabbitTemplate.convertAndSend("exchange-1","send", s, correlationData);
        return "send";

    }

}
package com.example.demo.controller;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RabbitReceiver {
    @RabbitListener(queues = "queue-1")
    public void receive(byte[] msg) {
        System.out.println("消费者收到了一个消息: " + new String(msg) + "  " + new Date().getTime());
    }
}

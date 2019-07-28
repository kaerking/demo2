package com.example.demo.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration

public class RabbitConfig {

//自动注入RabbitTemplate模板类

    @Autowired

    private RabbitTemplate rabbitTemplate;

    /**

     * 模版类定义

     * Jackson消息转换器

     * ConfirmCallback接口用于实现消息发送到RabbitMQ交换器后接收ack回调  即消息发送到exchange  ack

     * ReturnCallback接口用于实现消息发送到RabbitMQ交换器，但无相应队列与交换器绑定时的回调  即消息发送不到任何一个队列中  ack

     * @return  amqp template

     */

    @Bean

    public AmqpTemplate amqpTemplate() {

// 使用jackson 消息转换器

        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());

        rabbitTemplate.setEncoding("UTF-8");

// 开启returncallback    properties 需要 配置publisher-returns: true

        rabbitTemplate.setMandatory(true);

        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {

            String correlationId = message.getMessageProperties().getCorrelationId();

        });

//  消息确认  properties 需要配置publisher-returns: true

        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {

            if (ack) {

                SimpleDateFormat df =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

// 输出字符串

                System.out.println("时间"+df.format(new Date()));

                System.out.println("消息发送到exchange成功,id: "+correlationData.getId());
               // System.out.println("messge is :"+correlationData.getReturnedMessage().getBody().toString());

            }else {

                System.out.println("消息发送到exchange失败,原因: "+ cause);

            }

        });

        return rabbitTemplate;

    }

    /**

     * 声明Direct交换机 支持持久化.

     *

     * @return the exchange

     */

    @Bean

    public Exchange directExchange() {

        return ExchangeBuilder.directExchange("exchange-1").durable(true).build();

    }

    /**

     * 声明一个队列 支持持久化.

     * @return the queue

     */

    @Bean

    public Queue directQueue() {

        return QueueBuilder.durable("queue-1").build();

    }

    /**

     * 通过绑定键 将指定队列绑定到一个指定的交换机 .

     * @param queue    the queue

     * @param exchange the exchange

     * @return the binding

     */

    @Bean

    public Binding directBindingA( Queue queue, Exchange exchange) {

        return BindingBuilder.bind(queue).to(exchange).with("send").noargs();

    }

/*    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(ConnectionFactory connectionFactory){
        SimpleMessageListenerContainer simpleMessageListenerContainer =new SimpleMessageListenerContainer();
        simpleMessageListenerContainer.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
        return simpleMessageListenerContainer;
    }*/

}

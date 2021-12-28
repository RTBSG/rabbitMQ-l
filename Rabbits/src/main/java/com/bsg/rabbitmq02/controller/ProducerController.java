package com.bsg.rabbitmq02.controller;

import com.bsg.rabbitmq02.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 生产控制器
 *
 * @author bilib
 * @date 2021/08/06
 */
@RestController
@RequestMapping("/confirm")
@Slf4j
public class ProducerController {

    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @GetMapping("sendMessage/{message}")
    public void sendMessage(@PathVariable String message){
//        这个id来自框架内部CorrelationData的构造方法 可以为消息设置id

      CorrelationData correlationData =new CorrelationData("1");
        /* 故意出错 交换机名字出错
        Shutdown Signal: channel error; protocol method: #method<channel.close>(reply-code=404, reply-text=NOT_FOUND - no exchange 'confirm.exchange123' in vhost '/', class-id=60, method-id=40)
        2021-08-06 11:33:58.439  INFO 8896 --- [nectionFactory3] com.bsg.rabbitmq02.impl.MyCallBack       : 交换机还未收到 id 为:1消息,由于原因:channel error; protocol method: #method<channel.close>(reply-code=404, reply-text=NOT_FOUND - no exchange 'confirm.exchange123' in vhost '/', class-id=60, method-id=40)
*/
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                "key1", message,correlationData);
        log.info("发送消息内容:{},message",message);

        CorrelationData correlationData2 =new CorrelationData("2");
        /* 故意出错 队列名字出错
        交换机已经收到 id 为:2的消息
      : 交换机已经收到 id 为:1的消息
        接受到队列 confirm.queue 消息:莎莎H
        队列2未收到
         */

        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                "key52", message,correlationData2);
        log.info("发送消息内容:{},message",message);
    }

 }




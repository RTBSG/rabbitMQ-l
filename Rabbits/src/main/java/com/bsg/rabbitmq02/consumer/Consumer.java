package com.bsg.rabbitmq02.consumer;

import com.bsg.rabbitmq02.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消费者
 *发布确认
 * @author bilib
 * @date 2021/08/06
 */
@Component
@Slf4j
public class Consumer {

    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE_NAME)
    public void receiveMsg(Message message){
        String msg=new String(message.getBody());
        log.info("消费者队列接受消息:{}",msg);

    }
}

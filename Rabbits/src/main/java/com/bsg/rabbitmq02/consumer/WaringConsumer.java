package com.bsg.rabbitmq02.consumer;

import com.bsg.rabbitmq02.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 报警消费者
 *
 * @author bilib
 * @date 2021/08/06
 */
@Component
@Slf4j
public class WaringConsumer {
    @RabbitListener(queues = ConfirmConfig.WARING_QUEUE_NAME)
    public void receiveWaringMsg(Message message) {
        String msg = new String(message.getBody());
        log.error("报警消费者发现不可路由消息：{}", msg);
    }
}

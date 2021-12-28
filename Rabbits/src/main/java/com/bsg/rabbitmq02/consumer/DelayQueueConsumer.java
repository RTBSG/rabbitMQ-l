package com.bsg.rabbitmq02.consumer;

import com.bsg.rabbitmq02.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 延迟队列使用者
 *
 * @author bilib
 * @date 2021/08/05
 */
@Slf4j
@Component
public class DelayQueueConsumer {
    @RabbitListener(queues = DelayedQueueConfig.DELAYED_QUEUE_NAME)
    public void receiveDelayedQueue(Message message){
        String msg = new String(message.getBody());

        log.info("当前时间：{},收到延时队列的消息：{}", new Date().toString(), msg);
    }

}

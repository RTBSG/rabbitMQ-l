package com.bsg.rabbitmq02;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 确认配置
 *发布确认 生产者
 * @author bilib
 * @date 2021/08/06
 */
@Configuration
@Slf4j
public class ConfirmConfig {
    /**
     * 确认交易名称
     */
    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";

    public static final String CONFIRM_QUEUE_NAME = "confirm.queue";

    public static final String BACKUP_EXCHANGE_NAME = "backup.exchange";

    public static final String BACKUP_QUEUE_NAME = "backup.queue";
    public static final String WARING_QUEUE_NAME = "waring.queue";

//    声明备份交换机  一般声明为fanout类型，这样交换机收到路由不到队列的消息就会发送到备用交换机绑定的队列中
    @Bean("backupExchange")
    public FanoutExchange backupExchange(){
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }


    //声明主交换机 如果有备份交换机则需要转发到备份交换机上
    @Bean("confirmExchange")
    public DirectExchange confirmExchange(){
//        return new DirectExchange(CONFIRM_EXCHANGE_NAME);

/** new DirectExchange 直接创建交换机
 * 备份交换机创建
 *  ExchangeBuilder.directExchange(主交换机名).durable(持久化)
 *  .withArgument("alternate-exchange",备份交换机名).build();
 */
            return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME).durable(true)
                .withArgument("alternate-exchange",BACKUP_EXCHANGE_NAME).build();
    }


// 声明确认队列 自定义bean的命名 value 和name是一个东西
    @Bean("confirmQueue")
    public Queue confirmQueue(){
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }
    // 声明备份队列
    @Bean("backupQueue")
    public Queue backupQueue(){
        return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();
    }
    // 声明警告队列
    @Bean("waringQueue")
    public Queue waringQueue(){
        return QueueBuilder.durable(WARING_QUEUE_NAME).build();
    }


// 声明确认队列绑定关系

    @Bean
    public Binding queueBinding(@Qualifier("confirmQueue") Queue queue,
                                @Qualifier("confirmExchange") DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("key1");
    }
    // 声明备份队列绑定备份交换机 扇形广播是没有rabbitkey 不需要with 所有人都能收到
    @Bean
    public Binding backupQueueBindingBackupExchange(@Qualifier("backupQueue") Queue queue,
                                @Qualifier("backupExchange") FanoutExchange exchange){
        return BindingBuilder.bind(queue).to(exchange);
    }

    @Bean
    public Binding waringQueueBindingBackupExchange(@Qualifier("waringQueue") Queue queue,
                                                    @Qualifier("backupExchange") FanoutExchange exchange){
        return BindingBuilder.bind(queue).to(exchange);
    }

}


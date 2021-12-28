package com.bsg.rabbitmqspringboot.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;




import java.util.HashMap;

import java.util.Map;

/**
 * ttl队列配置
 *
 * @author bilib
 * @date 2021/08/05
 */
public class TtlQueueConfig {

    public static final String X_EXCHANGE = "X";
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";
    public static final String DEAD_LETTER_QUEUE = "QD";

    /**
     * x交换
     *声明xExchange交换机别名
     * @return {@link DirectExchange}
     */
    @Bean("xExchange")
    public DirectExchange xExchange() {
    return new DirectExchange(X_EXCHANGE);
    }

    @Bean("yExchange")
    public DirectExchange yExchange() {
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }

    /**声明普通队列 TTL为10s
     * queuea
     *设置死信交换机
     * 设置死信RountingKey
     * @return {@link Queue}
     */
    @Bean("queueA")
    public Queue queueA() {
        Map<String, Object> arguments = new HashMap<>();
//        过期时间 可以在死信设置，可以在生产者设置
        arguments.put("x-message-ttl", 10000);
//        正常队列设置死信交换机  arguments是正常队列的参数  中调用死信交换机
        arguments.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
//        设置死信RoutingKey
        arguments.put("x-dead-letter-routing-key", "YD");

        return QueueBuilder.durable(QUEUE_A).withArguments(arguments).build();
    }

    @Bean("queueB")
    public Queue queueB() {
        Map<String, Object> arguments = new HashMap<>();
//        过期时间 可以在死信设置，可以在生产者设置
        arguments.put("x-message-ttl", 40000);
//        正常队列设置死信交换机  arguments是正常队列的参数  中调用死信交换机
        arguments.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
//        设置死信RoutingKey
        arguments.put("x-dead-letter-routing-key", "YD");

        return QueueBuilder.durable(QUEUE_B).withArguments(arguments).build();
    }

    /**
     *  死信队列
     * @return {@link Queue}
     */
    @Bean("queueD")
    public Queue queueD(){

        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }

    /**
     * 队列a绑定X交换机
     *
     * @return {@link Binding}
     */
    @Bean
    public Binding queueABindingX(@Qualifier("queueA") Queue queueA,
                                  @Qualifier("xExchange") DirectExchange xExchange) {
//        队列a绑定X交换机
        return BindingBuilder.bind(queueA).to(xExchange).with("XA");

    }
    @Bean
    public Binding queueBBindingX(@Qualifier("queueB") Queue queueB,
                                  @Qualifier("xExchange") DirectExchange xExchange) {
//        队列B绑定X交换机
        return BindingBuilder.bind(queueB).to(xExchange).with("XB");

    }
    @Bean
    public Binding queueDBindingY(@Qualifier("queueD") Queue queueD,
                                  @Qualifier("yExchange") Exchange yExchange) {
//        队列D绑定Y交换机
        return BindingBuilder.bind(queueD).to(yExchange).with("YD").noargs();

    }


}

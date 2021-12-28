package com.bsg.six;

import com.bsg.Utils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * consumer01
 *
 * @author bilib
 * @date 2021/08/05
 */
public class Consumer01 {

    public static final String NORMAL_EXCHANGE = "normal_exchange";
    public static final String DEAD_EXCHANGE = "dead_exchange";
    public static final String NORMAL_QUEUE = "normal_queue";
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = Utils.getChannel();
//        声明交换机类型direct 死信 和普通
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
        Map<String, Object> arguments = new HashMap<>();
//        过期时间 可以在死信设置，可以在生产者设置
//        arguments.put("x-message-ttl", 10000);

//        正常队列设置死信交换机  arguments是正常队列的参数  中调用死信交换机
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
//        设置死信RoutingKey
        arguments.put("x-dead-letter-routing-key", "lisi");
/*
        置正常队列的长度限制，超过转为死信队列
        arguments.put("x-max-length", 6);
        */
//         声明普通和死信队列 死信需要参数
        channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);
        /**
         * 死信队列
         *
         */
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);

        /**绑定队列
         * 1.队列名
         * 2.交换机名
         * 3.rabbitkey
         */
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");

        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");


        //        声明接收消息
        DeliverCallback deliverCallback =(consumerTag, message)-> {
            String msg = new String(message.getBody(), "UTF-8");
            if ("info10".equals(msg)) {

                System.out.println("consumer01:" + msg +"此消息被拒绝");
                /**basicReject 消息应答的方法 用于否定确认
                 * false 将拒绝的消息转为死信队列，true 重新打回正常队列
                 */
                channel.basicReject(message.getEnvelope().getDeliveryTag(),false);
            }else {
                System.out.println("cousmer01:"+msg);
//                basicAck(用于肯定确认
                channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
            }
        };
//        开启手动应答，自动应答直接应答了
        channel.basicConsume(NORMAL_QUEUE, false, deliverCallback, consumerTag -> { });

    }



}

package com.bsg.five;

import com.bsg.Utils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 收到diretc01
 *
 * @author bilib
 * @date 2021/08/05
 */
public class ReceiveDiretc01 {
    public static final String EXCHANGE_NAME = "topic_log";
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = Utils.getChannel();
//        声明一个交换机 类型
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

//         声明一个队列
        channel.queueDeclare("Q1", false, false, false, null);

        /**绑定队列
         * 1.队列名
         * 2.交换机名
         * 3.rabbitkey
         */
        channel.queueBind("Q1", EXCHANGE_NAME, "*.orange.*");

        //        声明接收消息
        DeliverCallback deliverCallback =(consumerTag, message)-> {
            System.out.println("控制台打印接收的消息:"+new String(message.getBody(),"UTF-8"));
        };
            channel.basicConsume("Q1", true, deliverCallback, consumerTag -> { });
    }
}

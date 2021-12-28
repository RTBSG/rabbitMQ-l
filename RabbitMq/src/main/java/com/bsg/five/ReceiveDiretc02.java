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
public class ReceiveDiretc02 {
    public static final String EXCHANGE_NAME = "topic_log";
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = Utils.getChannel();
//        声明一个交换机 fanout类型
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

//         声明一个队列
       channel.queueDeclare("Q2", false, false, false, null);

//        多重  绑定队列
        channel.queueBind("Q2", EXCHANGE_NAME, "*.*.rabbit");
        channel.queueBind("Q2", EXCHANGE_NAME, "lazy.#");


        //        声明接收消息
        DeliverCallback deliverCallback =(consumerTag, message)-> {
            System.out.println("控制台打印接收的消息:"+new String(message.getBody(),"UTF-8"));
        };
        /**
         * 消费者接收消息
         * 1.消费那个队列
         * 2.消费成功之后是否自动应答 true 代表自动应答，false 手动应答
         * 3.消费者未成功接收的回调函数
         * 4.消费者成功接收的回调函数
         */
        channel.basicConsume("Q2", true, deliverCallback, consumerTag -> { });
    }
}

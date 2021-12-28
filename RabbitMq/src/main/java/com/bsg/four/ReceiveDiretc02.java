package com.bsg.four;

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
    public static final String EXCHANGE_NAME = "direct_logs";
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = Utils.getChannel();
//        声明一个交换机 fanout类型
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

//         声明一个队列
       channel.queueDeclare("console", false, false, false, null);

//        多重  绑定队列
        channel.queueBind("console", EXCHANGE_NAME, "info");
        channel.queueBind("console", EXCHANGE_NAME, "warning");


        //        声明接收消息
        DeliverCallback deliverCallback =(consumerTag, message)-> {
            System.out.println("控制台打印接收的消息:"+new String(message.getBody(),"UTF-8"));
        };
        channel.basicConsume("console", true, deliverCallback, consumerTag -> { });
    }
}

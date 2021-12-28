package com.bsg.two;

import com.bsg.Utils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * consumer2
 *
 * @author bilib
 * @date 2021/08/04
 */
public class Consumer2 {
    public static final String QUERY_NAME = "duileceshi";
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Channel channel = Utils.getChannel();

        //        声明接收消息
        DeliverCallback deliverCallback =(consumerTag, message)->{
//        接收的是对象的地址 com.rabbitmq.client.Delivery@6a11c0fd
            System.out.println(message);
            try {
                Thread.sleep(3 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            接收消息体
            System.out.println(new String(message.getBody()));
        };
//       取消消息的回调
        CancelCallback cancelCallback = consumerTag->{

            System.out.println("消费消息中断了");
        };
        System.out.println("消费者2 睡眠3s");
       channel.basicQos(3);

        channel.basicConsume(QUERY_NAME, false, deliverCallback, cancelCallback);

    }

}



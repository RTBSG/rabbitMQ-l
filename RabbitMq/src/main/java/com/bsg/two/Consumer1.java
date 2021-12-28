package com.bsg.two;

import com.bsg.Utils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * consumer1
 *
 * @author bilib
 * @date 2021/08/04
 */
public class Consumer1 {
    public static final String QUERY_NAME = "duileceshi";
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Channel channel = Utils.getChannel();

        //        声明接收消息
        DeliverCallback deliverCallback =(consumerTag, message)->{
//        接收的是对象的地址 com.rabbitmq.client.Delivery@6a11c0fd
            System.out.println(message);
            try {
                Thread.sleep(1 * 1000);
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
        System.out.println("消费者1 睡眠1s");
    /*        默认轮询 采用不公平分发能者多劳
              1 为不公平分发
              2 为预取值分发 除1以外都是预取值的数量 2分发给这个线程两条信息处理
              */
                channel.basicQos(5);
        channel.basicConsume(QUERY_NAME, false, deliverCallback, cancelCallback);

    }

}

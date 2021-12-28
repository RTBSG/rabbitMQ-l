package com.bsg.four;

import com.bsg.Utils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * 发出日志
 *
 * @author bilib
 * @date 2021/08/04
 */
public class DirectLogs {
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = Utils.getChannel();

        System.out.println("请输入：");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message=scanner.next();
            channel.basicPublish(EXCHANGE_NAME, "error", null, message.getBytes("UTF-8"));
            System.out.println("生产者发出消息"+message);
        }


    }
}

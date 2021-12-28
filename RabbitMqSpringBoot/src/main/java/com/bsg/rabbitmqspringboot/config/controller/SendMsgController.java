package com.bsg.rabbitmqspringboot.config.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 送味精控制器
 *
 * @author bilib
 * @date 2021/08/05
 */
@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMsgController {

    /**
     * 框架提供
     */
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 送味精
     * http://localhost:8080/ttl/sendMsg/嘻嘻嘻
     * @param message 消息
     */
    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable String message) {
        //这两个{}相当于占位符 将后面的两个参数填充进去
        log.info("当前时间:{}，发送一条消息给两个ttl队列:{}", new Date().toString(), message);
        rabbitTemplate.convertAndSend("X", "XA", "消息来自ttlwei10s的队列" + message);
        rabbitTemplate.convertAndSend("X", "XB", "消息来自ttlwei40s的队列" + message);





    }












}

package com.debug.middleware.server;

import com.debug.middleware.server.rabbitmq.publisher.BasicPublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Tang on 2020/7/23
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class RabbitmqTest {
    //定义JSON序列化和反序列实例
    @Autowired
    private ObjectMapper objectMapper;
    //定义基本消息模型中发送消息的生产者实例
    @Autowired
    private BasicPublisher basicPublisher;

    @Test
    public void test1() throws Exception {
        //定义字符串值
        String msg = "~~~~这是一串字符串消息~~~~~";
        //生产者实例发送消息
        basicPublisher.sendMsg(msg);
    }
}

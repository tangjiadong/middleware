package com.debug.middleware.server;

import com.debug.middleware.server.event.Publisher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Tang on 2020/7/21
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class EventTest {
    //自动装配生产者实例
    @Autowired
    private Publisher publisher;

    @Test
    public void test1() throws Exception{
        //调用发送消息的方法产生消息
        publisher.sendMsg();
    }
}

package com.debug.middleware.server;

import com.debug.middleware.server.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Tang on 2020/7/14
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class StringRedisTemplateTest {
    //定义日志
    private static final Logger log = LoggerFactory.getLogger(RedisTemplateTest.class);
    @Autowired
    private RedisTemplate redisTemplate;
    //定义JSON序列化与反序列化框架类
    @Autowired
    private ObjectMapper objectMapper;
    //定义StringRedisTemplate操作组件
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //采用StringRedisTemplate将一字符串信息写入缓存中,并读取出来
    @Test
    public void three(){
        log.info("-----开始StringRedisTemplate操作组件实战");
        //定义字符串内容以及存入缓存的key
        final String key ="StringRedisString";
        final String content = "StringRedisTemplate Content";
        //Redis通用的操作组件
        ValueOperations valueOperations = stringRedisTemplate.opsForValue();
        log.info("content",content);
        valueOperations.set(key,content);
        Object result = valueOperations.get(key);
        log.info("读取出来的内容:{ } ",result);
    }

    @Test
    public void four() throws Exception{
        log.info("-----开始RedisTemplate操作组件实战------");
        User user = new User(2,"jiadong","tang");
        ValueOperations valueOperations = stringRedisTemplate.opsForValue();
        //将序列化后的信息写入缓存中
        final String key = "User2";
        final String content = objectMapper.writeValueAsString(user);
        valueOperations.set(key,content);
        log.info("写入缓存对象的信息:{}",user);
        //从缓存中读取内容
        Object result = valueOperations.get(key);
        if (result != null){
            User resultUser = objectMapper.readValue(result.toString(), User.class);
            log.info("读取缓存内容并反序列化后的结果:{}",resultUser);
        }
    }
}

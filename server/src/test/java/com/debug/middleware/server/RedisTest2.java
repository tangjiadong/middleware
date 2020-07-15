package com.debug.middleware.server;

import com.debug.middleware.server.entity.Fruit;
import com.debug.middleware.server.entity.PhoneUser;
import com.debug.middleware.server.entity.Student;
import com.debug.middleware.server.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by Tang on 2020/7/14
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RedisTest2 {
    //定义日志
    private static final Logger log = LoggerFactory.getLogger(RedisTemplateTest.class);
    @Autowired
    private RedisTemplate redisTemplate;
    //JSON序列化与反序列化框架类
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Redis:字符串
     * @throws Exception
     */
    @Test
    public void one() throws Exception{
        User user = new User(1,"tang","jiadong");
        //定义key与即将存入缓存的value
        final String key = "userString";
        String value = objectMapper.writeValueAsString(user);
        //Redis通用的操作组件
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //写入缓存
        log.info("存入缓存中用户实体对象信息为:{}",key);
        valueOperations.set(key,value);
        //从缓存中获取用户实体信息
        Object res = redisTemplate.opsForValue().get(key);
        if (res!=null){
            User resUser = objectMapper.readValue(res.toString(), User.class);
            log.info("从缓存中读取信息:{}",resUser);
        }
    }

    /**
     * Redis:列表
     * 应用:排行榜,排名
     * push()方法添加,pop()方法获取数据
     * @throws Exception
     */
    @Test
    public void two() throws Exception{
        //构造已经排好序的用户对象列表
        List<User> list = new ArrayList<>();
        list.add(new User(10,"yang","杨"));
        list.add(new User(11,"hu","胡"));
        list.add(new User(12,"shen","申"));
        log.info("构造已经排好序的用户对象列表:{}",list);
        //将列表中的数据存储至Redis的List中
        final String key = "userList";
        ListOperations listOperations = redisTemplate.opsForList();
        for (User user : list) {
            //往列表中添加数据(从队尾添加)
            listOperations.leftPush(key,user);
        }
        //获取Redis中List的数据(从队头中遍历获取,直到没有元素为止)
        log.info("---获取Redis中List的数据-从队头中获取--");
        Object res = listOperations.rightPop(key);
        User resUser;
        while (res!=null){
            resUser = (User)res;
            log.info("当前数据:{}",resUser);
            res = listOperations.rightPop(key);
        }
    }

    /**
     * Redis:集合
     * 保证存储数据的唯一,不重复
     * 应用:解决重复提交,剔除重复id
     * @throws Exception
     */
    @Test
    public void three() throws Exception{
        //构造一组用户姓名列表
        List<String> userList = new ArrayList<>();
        userList.add("tang");
        userList.add("jia");
        userList.add("jia");
        userList.add("dong");
        userList.add("dong");
        userList.add("汤");
        userList.add("jia");
        log.info("待处理的用户姓名列表:{}",userList);
        //遍历访问,剔除相同姓名的用户并写入集合,最终存入缓存
        final String key = "userCollections";
        SetOperations setOperations = redisTemplate.opsForSet();
        for (String str:userList){
            setOperations.add(key,str);
        }

        //从缓存中获取用户对象集合
        Object res = setOperations.pop(key);
        while (res!=null){
            log.info("从缓存中获取的用户集合-当前用户:{}",res);
            res = setOperations.pop(key);
        }
    }

    /**
     * Redis: SortedSet
     * 应用:充值排行榜,积分排行榜
     * range():正序 reverseRange():倒序
     * @throws Exception
     */
    @Test
    public void four() throws Exception{
        //构造一组无序的用户手机充值对象列表
        List<PhoneUser> list = new ArrayList<>();
        list.add(new PhoneUser("103",133.0));
        list.add(new PhoneUser("101",120.0));
        list.add(new PhoneUser("102",123.0));
        list.add(new PhoneUser("99",133.0));
        log.info("构造一组无序的用户手机充值对象列表:{}",list);
        //遍历访问充值对象列表,将信息写入Redis的有序集合
        final String key = "phoneUserZSet";
        //获取有序集合SortedSet操作组件ZSetOperations
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        for (PhoneUser phoneUser:list){
            //将元素添加进有序集合中
            zSetOperations.add(key,phoneUser,phoneUser.getMoney());
        }
        //前端获取访问充值排名靠前的用户列表
        Long size = zSetOperations.size(key);
        //从小到大排序
        Set<PhoneUser> resSet = zSetOperations.range(key, 0L, size);
        //从大到小排序
        //Set resSet = zSetOperations.reverseRange(key, 0L, size);
        //遍历获取有序集合中的元素
        for (PhoneUser u:resSet){
            log.info("从缓存中读取手机充值记录排序列表,当前记录:{}",u);
        }
    }

    /**
     * Redis: Hash
     * @throws Exception
     */
    @Test
    public void five() throws Exception{
        //构造学生对象列表和水果对象列表
        List<Student> students = new ArrayList<>();
        List<Fruit> fruits = new ArrayList<>();
        //往学生集合中添加学生对象
        students.add(new Student("10010","debug","tang"));
        students.add(new Student("10011","jack","jia"));
        students.add(new Student("10012","sam","dong"));
        //往水果集合中添加水果对象
        fruits.add(new Fruit("apple","红色"));
        fruits.add(new Fruit("orange","橙色"));
        fruits.add(new Fruit("banana","黄色"));
        //分别遍历不同对象列表,并采用哈希存储至缓存
        final String sKey = "studentKey";
        final String fKey = "fruitKey";
        //获取哈希存储操作组件HashOperations,遍历获取集合中的对象并添加进缓存
        HashOperations hashOperations = redisTemplate.opsForHash();
        for (Student s : students) {
            hashOperations.put(sKey,s.getId(),s);
        }
        for (Fruit f : fruits) {
            hashOperations.put(fKey,f.getName(),f);
        }
        //获取学生对象和水果对象列表
        Map<String,Student> sMap = hashOperations.entries(sKey);
        log.info("获取学生对象列表:{}",sMap);
        Map<String,Fruit> fMap = hashOperations.entries(fKey);
        log.info("获取水果对象列表:{}",fMap);

        //获取指定的学生对象
        String sField = "10012";
        Student s = (Student)hashOperations.get(sKey, sField);
        log.info("获取指定的学生对象:{}->{}",sField,s);
        //获取指定的水果对象
        String fField = "orange";
        Fruit f = (Fruit) hashOperations.get(fKey, fField);
        log.info("获取指定的水果对象:{}->{}",fField,f);
    }

    /**
     * Key失效一
     * 调用SETEX()方法中指定key的过期时间
     */
    @Test
    public void six() throws Exception{
        //构造key与Redis操作组件ValueOperations
        final String key="redisKey1";
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //第1种方法:在往缓存中放置数据时,提供一个TTL,表示TTL一到,缓存中的key自动失效,即被清理
        valueOperations.set(key,"expire操作",10L, TimeUnit.SECONDS);
        //等待5秒,判断key是否还存在
        Thread.sleep(5000);
        Boolean existKey = redisTemplate.hasKey(key);
        Object value = valueOperations.get(key);
        log.info("等待5秒-判断key是否还存在:{} 对应的值:{}",existKey,value);

        //再等待5秒,判断key是否存在
        Thread.sleep(5000);
        existKey = redisTemplate.hasKey(key);
        value = valueOperations.get(key);
        log.info("再等待5秒-判断key是否还存在:{} 对应的值:{}",existKey,value);
    }

    /**
     * Key失效二
     * 调用SETEX()方法中指定key的过期时间
     */
    @Test
    public void seven() throws Exception{
        //构造key与Redis操作组件ValueOperations
        final String key="redisKey1";
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //第1种方法:在往缓存中放置数据时,提供一个TTL,表示TTL一到,缓存中的key自动失效,即被清理
        //valueOperations.set(key,"expire操作",10L, TimeUnit.SECONDS);
        //第2种方法:在往缓存中放置数据后,采用RedisTemplate的Expire()方法
        valueOperations.set(key,"expire操作-2");
        redisTemplate.expire(key,10L,TimeUnit.SECONDS);
        //等待5秒,判断key是否还存在
        Thread.sleep(5000);
        Boolean existKey = redisTemplate.hasKey(key);
        Object value = valueOperations.get(key);
        log.info("等待5秒-判断key是否还存在:{} 对应的值:{}",existKey,value);

        //再等待5秒,判断key是否存在
        Thread.sleep(5000);
        existKey = redisTemplate.hasKey(key);
        value = valueOperations.get(key);
        log.info("再等待5秒-判断key是否还存在:{} 对应的值:{}",existKey,value);
    }
}

package com.ypy.matebackend;

import com.ypy.matebackend.entity.User;
import com.ypy.matebackend.service.UserActionService;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedisTest {
    @Resource
    private RedisTemplate redisTemplate;

    @Test
    void testRedis() {
//        ValueOperations valueOperations = redisTemplate.opsForValue();  // 操作值的集合
//        // create
//        valueOperations.set("习近平", "皇上");
//        valueOperations.set("李强", "太监");
//        // check
//        Object xjp = valueOperations.get("习近平");
//        System.out.println((String) xjp);
//        Object lq = valueOperations.get("李强");
//        System.out.println((String) lq);
    }

    @Resource
    private RedissonClient redisson;

    @Test
    void testRedisson() {
//        RList<String> rList = redisson.getList("test");
//        rList.add("习近平");
//        rList.add("邓小平");
//        rList.add("江泽民");
//        for (int i = 0; i < 3; i++) {
//            System.out.println(rList.get(i));
//        }
    }

    @Resource
    private UserActionService userActionService;

    @Test
    void loadRedisData() {
        // 调试前执行它来加载 redis 数据
//        List<User> activeUsers = userActionService.getActiveUsers();
//        String redisKey = "mate:precache-job:active-users";
//        redisTemplate.opsForValue().set(redisKey, activeUsers, 70, TimeUnit.MINUTES);  // 缓存有效期设置为70分钟
    }
}

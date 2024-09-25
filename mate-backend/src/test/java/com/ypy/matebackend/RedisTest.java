package com.ypy.matebackend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

@SpringBootTest
public class RedisTest {
    @Resource
    private RedisTemplate redisTemplate;

    @Test
    void test() {
        ValueOperations valueOperations = redisTemplate.opsForValue();  // 操作值的集合
        // create
        valueOperations.set("习近平", "皇上");
        valueOperations.set("李强", "太监");
        // check
        Object xjp = valueOperations.get("习近平");
        System.out.println((String) xjp);
        Object lq = valueOperations.get("李强");
        System.out.println((String) lq);
    }
}

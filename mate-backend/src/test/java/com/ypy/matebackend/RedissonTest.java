package com.ypy.matebackend;

import org.junit.jupiter.api.Test;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class RedissonTest {

    @Resource
    private RedissonClient redisson;

    @Test
    void test() {
//        RList<String> rList = redisson.getList("test");
//        rList.add("习近平");
//        rList.add("邓小平");
//        rList.add("江泽民");
//        for (int i = 0; i < 3; i++) {
//            System.out.println(rList.get(i));
//        }
    }
}

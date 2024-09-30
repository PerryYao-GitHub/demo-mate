package com.ypy.matebackend.service;

import com.ypy.matebackend.common.Resp;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserActionServiceTest {
    @Resource
    private UserActionService userActionService;

    @Test
    void searchUsersByTags() {
//        List<String> tags = Arrays.asList("java", "music");
//        Resp resp = userActionService.searchUsersByTags(tags);
//        System.out.println(resp.getData());
    }
}

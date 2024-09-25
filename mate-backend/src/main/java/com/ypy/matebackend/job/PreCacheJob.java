package com.ypy.matebackend.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ypy.matebackend.entity.User;
import com.ypy.matebackend.service.UserService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class PreCacheJob {
    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    private List<Integer> importantUserIds = Arrays.asList(1, 2, 3);  // 重点用户

    /**
     * 定时向 redis 中加载 访客用户 看到的数据 -> redisKey = "mate-user-recommend-{page}", 其中 page 可以选取 1, 2 两页
     * 还要向 redis 中加载 重点用户 看到的数据 -> redisKey = "mate-user-recommend-{page}-{importantUserId}"
     */
    @Scheduled(cron = "0 59 23 * * *")
    public void doPreCacheJob() {
        for (int page = 1; page <= 2; page ++) {
            String redisKey = "mate-user-recommend-" + page;
            QueryWrapper<User> qw = new QueryWrapper<>();
            IPage<User> usersPage = userService.page(new Page<>(page, 10), qw);  // 公共推荐部分
            List<User> users = usersPage.getRecords();
            redisTemplate.opsForValue().set(redisKey, users, 10, TimeUnit.MINUTES);
            for (Integer userId : importantUserIds) {
                String redisKeyForUser = redisKey + "-" + userId;
//                qw  特殊推送条件
                usersPage = userService.page(new Page<>(page, 10), qw);
                users = usersPage.getRecords();
                redisTemplate.opsForValue().set(redisKeyForUser, users, 10, TimeUnit.MINUTES);
            }
        }
    }
}

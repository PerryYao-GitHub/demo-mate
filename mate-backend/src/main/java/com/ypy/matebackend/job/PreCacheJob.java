package com.ypy.matebackend.job;

import com.ypy.matebackend.entity.User;
import com.ypy.matebackend.service.UserActionService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class PreCacheJob {
    @Resource
    private UserActionService userActionService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RedissonClient redisson;

    @Scheduled(cron = "0 0 * * * *")  // 每小时执行一次
    public void doPreCacheJob() {
        RLock rLock = redisson.getLock("mate-precachejob-doprecachejob-lock");
        try {
            if (rLock.tryLock(0L, 5L /*-1L 看门狗机制*/, TimeUnit.SECONDS)) {  // 抢到锁了就执行更新任务
                // 查找 "常用" 用户 => 登陆时间距离当下 <= 3 天 并且每小时更新
                List<User> activeUsers = userActionService.getActiveUsers();
                String redisKey = "mate-precachejob-activeusers";
                redisTemplate.opsForValue().set(redisKey, activeUsers, 70, TimeUnit.MINUTES);  // 缓存有效期设置为70分钟

                /**
                 * 缓存的有效期确实应该设置得稍微大于任务的执行间隔，即大于 1 小时。
                 * 因为任务的执行和缓存更新确实需要一定的时间，
                 * 如果缓存过期时间正好是 1 小时，
                 * 可能会出现任务执行过程中缓存已经失效但新数据还未写入 Redis 的情况。
                 * 建议将缓存时间设置为稍微大于 1 小时，比如 65 分钟或者 70 分钟，这样可以避免缓存过期问题。
                 */

//                // 使用 Redisson 设置 Redis 缓存
//                RBucket<List<User>> bucket = redisson.getBucket(redisKey);
//                bucket.set(activeUsers, 70, TimeUnit.MINUTES); // 设置缓存并定义过期时间为70分钟



            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {  // 释放锁的代码很重要, 必须一定要执行
            // 只能释放当前线程加的锁
            if (rLock.isHeldByCurrentThread()) rLock.unlock();
        }
    }
}

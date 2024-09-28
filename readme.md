# Mate System

brief: to help people find their partner

## Demanding Analysis

1. add tag, change tag, divide tag
2. search tag
   1. Redis
3. make group
   1. create group
   2. join group
   3. search group by tag
   4. invite others into group
4. recommend
   1. 相似度算法 + 本地分布式

## Tach Stack

### Frontend

1. Vue3
2. Vant UI (基于Vue的移动端组件库)
3. Vite 打包工具
4. Nginx单机部署

### Backend

1. Java + SpringBoot
2. SpringMVC + MyBatis Plus
3. MySQL + Redis
4. Swagger + Knife4j

# Notes

## 用户表中的tag

1. 直接建立 tags 字段; 
   1. 优点: 查用户的tag非常容易
   2. 缺点: 查tag下的用户比较不方便
   3. 为了解决以上缺点 ---> Redis
   4. 定义方法就是搞一个很长的 varchar
2. 建立关联表
   1. 优点: 可以正查反查
   2. 缺点: 要多维护一个表
   3. 大项目尽量减少关联查询 !!!!! 很影响扩展性和性能

## 后端接口

SQL查询

内存查询 -> 解析JOSN字符串

- 反序列化: JOSN -> JAVA OBJ (Maven 库: )
- 序列化: JAVA OBJ -> JSON (Maven 库: fastjson, gson (google), jackson, kryo)

如果参数可分析, 根据用户的参数数量选择查询方法

如果参数不可分析, 可以使用并发, 谁先返回用谁

还可以SQL查询与内存计算结合, 比如先用SQL筛出部分数量很多的标签

## 接口文档

线上环境要关闭接口文档

```xml
        <!-- api 文档 -->
<!--        <dependency>-->
<!--            <groupId>org.springdoc</groupId>-->
<!--            <artifactId>springdoc-openapi-ui</artifactId>-->
<!--            <version>1.6.4</version>-->
<!--        </dependency>-->
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger2</artifactId>
            <version>2.7.0</version>
        </dependency>

        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger-ui</artifactId>
            <version>2.7.0</version>
        </dependency>
```

在 spring boot 2.7.x 使用

注意, 可以使用 @Profile("prod" / "dev") 来关闭dev环境下的拦截器, 不然就得把所有的api doc 接口排除出拦截器, 不方便

## Redis

基于内存的 K/V 数据库, 读取用户的Session信息极为频繁, 故选择 Redis

引入redis

`org.springframework.boot` -> `spring-boot-starter-data-redis`

引入session与redis的整合工具

`org.springframework.session` -> `spring-data-redis`

调整session存储位置

````yml
spring:
  session:
    timeout: 1440m
    store-type: redis
````

认证的方法

session-cookie

- session - 本地内存
- session - redis (分布式session 登录)
- session -mysql (类似Django 自带的session处理方案)

jwt

Spring Security : 前后端不分离时期的产物, 只适合做细粒度的权限控制

### 主页功能

1. 默认推荐和自己tag相关或接近的用户
2. 优化主页性能 (缓存 + 定时任务 + 分布式锁)

#### 查询优化

从 MySQL 每次查询全体用户 (分页) 太慢且太重复, 因为每一个游客来到 home 界面都要拉取

可以先把全体用户数据查出来 (缓存)

但是第一个用户就会很慢, 所以采用预加载缓存 (定时更新缓存 00:00, 定时任务)

分布式锁: 控制同一时间只有一台机器去执行

当数据量过大 50w + , 即使用分页, 查询也比较慢 -> 换一个地方来查询 !!!

#### 缓存的实现

Redis

memcached

---

echch

## Redis

NoSQL + K/V 存储系统

### Java 里的操作方法

- Spring Data Redis
  - Spring Data: 定义了一组通用的数据库增删改查接口, 支持多种数据库
- Jedis 对立于 SpringBoot
- Redission 分布式操作 redis

### 数据结构

String

List

Set

Hash

Zset: 比set多了一个分数 {"aaa-9", "bbb-8"} 排行榜



### 缓存预热

缓存预热解决: 用户第一次查看依然很慢

缺点:

- 预热时机很重要
- 占用空间

方法

- 定时
- 手动触发 (双十一前)

定时任务

- Spring Scheduler
- Quartz
- XXL-job 分布式调度平台 (界面 + sdk)

用定时任务, 每天刷新用户推荐的列表 (数据总量多, 但每日变化不大)

要控制定时任务的执行, 在多服务器时, 多服务器同时发出定时任务更新 redis, 很浪费资源 + 重复插入

### 锁 / 分布式锁

只有抢到锁的更新任务才能执行

本地锁 / 分布式锁 redis setnx

锁: 用完一定要释放, 一定要加过期时间

### Redisson 实现分布式锁

redisson - java中操作redis的客户端, 可以让开发者像使用本地集合一样使用redis

redisson 中的续期机制 看门狗

- 监听线程 每十秒续期一次
- 除非线程挂了

多个 redis -> redlock

## 推荐算法

- 编辑距离算法
- 余弦相似度 (带权重)


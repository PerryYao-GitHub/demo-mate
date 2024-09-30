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



### 方法1：按分页来存储用户的推荐信息

**逻辑**：

- 为每一页的推荐用户单独存储在 Redis 中，例如：`mate-user-recommend-{page}-{userId}`。
- 每次用户请求新的页面时，`getMatchUsers` 可能会被再次调用，以获取匹配度较好的用户，并根据当前页过滤和分页。

**优点**：

1. **快速读取**：每一页的推荐信息已经缓存，用户直接请求时可以快速从 Redis 获取，无需重新分页计算。
2. **分散内存占用**：由于每次只存储一页的数据，可以避免一次性将所有匹配用户全部缓存，这对内存占用较少。

**缺点**：

1. **重复计算**：对于每一页的请求，`getMatchUsers` 都可能会再次执行。如果计算成本较高，这将导致重复计算，影响性能。
2. **分页复杂**：在每次计算分页时，还需要过滤掉前面页的用户，进一步加大计算复杂度。尤其当用户多次翻页时，会变得低效。

### 方法2：仅存储所有匹配用户，分页在读取时处理

**逻辑**：

- 只针对目标用户缓存其对应的所有推荐用户的完整列表，例如 `mate-user-recommend-{userId}`，当用户请求分页时，从这个缓存中进行分页。

**优点**：

1. **避免重复计算**：只需要一次执行 `getMatchUsers`，然后将所有匹配的用户列表缓存。用户请求不同页时，直接从缓存中读取，进行内存中的分页处理，减少重复的算法计算。
2. **分页灵活**：分页逻辑可以完全放在应用层，所有用户匹配的数据已经缓存，无需再考虑不同页之间的过滤和重新计算。
3. **高效**：当推荐用户总量不大时，这种方式更高效，因为计算只执行一次。

**缺点**：

1. **内存占用**：如果每个用户都有大量匹配用户，存储每个用户的完整推荐列表可能会占用较多内存。对于活跃用户较多的系统，可能会对 Redis 的内存消耗带来压力。
2. **不适合数据量非常大的场景**：如果匹配用户列表特别大，分页处理会有性能瓶颈。

### 取舍分析

#### 当你选择 **方法1**（按分页缓存）时：

- **适用场景**：如果每个用户的匹配用户数较多，且系统压力在 **内存占用** 上更大时，可以使用这种方式。这样可以避免缓存太多数据，但需要接受算法的重复计算和分页逻辑的复杂度。
- **适合的场景**：用户数量多，且分页的粒度较小（例如每页10个用户），每次请求页面时可以快速返回，而不会造成较大的内存压力。

#### 当你选择 **方法2**（存储完整匹配结果，分页时处理）时：

- **适用场景**：如果你希望减少推荐算法的重复计算，同时系统内存允许存储较多的数据，可以选择这种方式。这样可以在首次计算后直接缓存所有数据，后续所有分页操作只需要从缓存中读取，性能较高。
- **适合的场景**：用户量适中，推荐匹配度计算成本高，用户的匹配列表长度不会特别长。

### 我建议的取舍：

1. **如果推荐用户列表较短（如几十个用户）**，你可以考虑 **方法2**。这种方式能够显著减少计算成本，只需要在用户登录时执行一次推荐算法，之后直接分页处理即可，带来性能提升。
2. **如果推荐用户列表较长（如几百甚至上千个用户）**，且内存占用是个瓶颈，可以考虑 **方法1**，将每页结果单独存储，但需要在设计中优化重复计算（例如增加缓存时间，减少频繁重新计算）。

### 额外建议：

- **分页后的缓存策略**：可以考虑对热门用户的第一页做更多缓存，因为第一页的数据更常被访问。
- **LRU策略**：使用 Redis 的 LRU 策略自动清理过期的推荐缓存，避免内存占用过大。

总的来说，选择方法取决于你的应用场景和数据规模。如果系统允许，**存储完整推荐列表并在内存中分页** 是更优的选择，尤其在你希望避免算法的重复执行时。


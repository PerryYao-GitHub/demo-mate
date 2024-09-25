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
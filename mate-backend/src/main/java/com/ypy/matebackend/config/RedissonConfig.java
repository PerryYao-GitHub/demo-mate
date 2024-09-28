package com.ypy.matebackend.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedissonConfig {
    private String host;
    private Integer port;

    @Bean
    public RedissonClient redissonCli() {
        Config config = new Config();
        String address = String.format("redis://%s:%s", host, port);
        config.useSingleServer().setAddress(address).setDatabase(1);

        return Redisson.create(config);
    }
}

package com.jjp.config;

import dev.langchain4j.community.store.memory.chat.redis.RedisChatMemoryStore;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: jjp
 * @CreateTime: 2026-01-16
 * @Description: langchain4j-redis 配置类
 * @Version: 1.0
 */

@Configuration
@ConfigurationProperties(prefix = "spring.data.redis")
@Data
public class RedisChatMemoryStoreConfig {

    /**
     * Redis服务器主机地址
     */
    private String host;

    /**
     * Redis服务器端口
     */
    private int port;

    /**
     * Redis键的过期时间（秒）
     */
    private long ttl;

    @Bean
    public RedisChatMemoryStore redisChatMemoryStore() {
        return RedisChatMemoryStore.builder()
                .host(host)
                .port(port)
                .ttl(ttl)
                .build();
    }
}

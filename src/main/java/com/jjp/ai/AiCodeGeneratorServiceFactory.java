package com.jjp.ai;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.jjp.service.ChatHistoryService;
import dev.langchain4j.community.store.memory.chat.redis.RedisChatMemoryStore;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * @Author: jjp
 * @CreateTime: 2026-01-14
 * @Description: AI代码生成器服务工厂类
 * @Version: 1.0
 */

@Slf4j
@Configuration
public class AiCodeGeneratorServiceFactory {

    // 注入ChatModel Bean，用于与AI模型进行交互
    @Resource
    private ChatModel chatModel;

    // 注入StreamingChatModel Bean，用于支持流式响应
    @Resource
    private StreamingChatModel streamingChatModel;

    // 注入RedisChatMemoryStore Bean，用于存储聊天记录
    @Resource
    private RedisChatMemoryStore redisChatMemoryStore;

    // 对话历史服务
    @Resource
    private ChatHistoryService chatHistoryService;

    // 使用Caffeine缓存框架创建一个Long类型的键与AiCodeGeneratorService类型的值的缓存映射
    private final Cache<Long, AiCodeGeneratorService> serviceCache = Caffeine.newBuilder()
            // 设置缓存最大容量为1000，当缓存项数量超过这个值时，会触发缓存项的移除
            .maximumSize(1000)
            // 设置缓存写入后30分钟过期，即最后一次写入操作后30分钟缓存失效
            .expireAfterWrite(Duration.ofMinutes(30))
            // 设置缓存最后访问后10分钟过期，即最后一次访问操作后10分钟缓存失效
            .expireAfterAccess(Duration.ofMinutes(10))
            // 添加缓存项移除监听器，当缓存项被移除时会触发回调
            .removalListener(
                    (key, value, cause) -> {
                        // 记录缓存项被移除的日志，包括appId和移除原因
                        log.info("AI代码生成器服务实例被移除，appId:{}, cause:{}", key, cause);
                    }
            )
            // 构建缓存实例
            .build();

//    /**
//     * 创建并配置AI代码生成器服务的Bean
//     * @return 返回一个配置好的AiCodeGeneratorService实例
//     */
//    @Bean
//    public AiCodeGeneratorService aiCodeGeneratorService() {
//        return AiServices.create(AiCodeGeneratorService.class, chatModel);
//    }

    /**
     * 创建并配置AI代码生成器服务的Bean（流式输出）
     * @return 返回一个配置好的AiCodeGeneratorService实例
     */
    @Bean
    public AiCodeGeneratorService aiCodeGeneratorService() {
        return getAiCodeGeneratorService(0L);
    }

    /**
     * 根据appId获取独立的AI代码生成器服务实例
     * @param appId 应用ID
     * @return 返回一个独立的AI代码生成器服务实例
     */
    public AiCodeGeneratorService getAiCodeGeneratorService(long appId) {
        // 根据appId构建独立的对话记忆
        // 使用serviceCache缓存服务实例，如果不存在则调用createAiCodeGeneratorService方法创建
        return serviceCache.get(appId, this::createAiCodeGeneratorService);
    }

    /**
     * 创建AI代码生成器服务实例
     * @param appId 应用ID
     * @return 返回新创建的AI代码生成器服务实例
     */
    public AiCodeGeneratorService createAiCodeGeneratorService(long appId) {
        // 记录创建服务实例的日志
        log.info("创建AI代码生成器服务实例，appId:{}", appId);
        // 根据appId构建独立的对话记忆，确保每个应用有独立的上下文
        // 使用MessageWindowChatMemory创建具有固定窗口大小的对话记忆
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory
                .builder()
                .id(appId) // 设置记忆ID为appId，确保每个应用有独立的对话记忆
                .chatMemoryStore(redisChatMemoryStore) // 使用Redis作为持久化存储，实现记忆的共享和持久化
                .maxMessages(20) // 设置最大记忆消息数为20，控制上下文长度
                .build();
        // 从数据库加载历史对话到记忆中
        chatHistoryService.loadChatHistoryToMemory(appId, chatMemory, 20);
        // 使用AiServices构建器模式创建AI代码生成器服务实例
        return AiServices
                .builder(AiCodeGeneratorService.class)
                .chatModel(chatModel) // 配置聊天模型
                .streamingChatModel(streamingChatModel) // 配置流式聊天模型，支持流式响应
                .chatMemory(chatMemory) // 配置对话记忆，维护上下文
                .build();
    }
}

package com.jjp.ai;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.jjp.ai.tools.*;
import com.jjp.exception.BusinessException;
import com.jjp.exception.ErrorCode;
import com.jjp.model.enums.CodeGenTypeEnum;
import com.jjp.service.ChatHistoryService;
import dev.langchain4j.community.store.memory.chat.redis.RedisChatMemoryStore;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
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
    private StreamingChatModel openAiStreamingChatModel;

    // 注入RedisChatMemoryStore Bean，用于存储聊天记录
    @Resource
    private RedisChatMemoryStore redisChatMemoryStore;

    // 对话历史服务
    @Resource
    private ChatHistoryService chatHistoryService;

    // 流式对话模型
    @Resource
    private StreamingChatModel reasoningStreamingChatModel;

    // 工具管理器
    @Resource
    private ToolManager toolManager;

    // 使用Caffeine缓存框架创建一个缓存映射
    private final Cache<String, AiCodeGeneratorService> serviceCache = Caffeine.newBuilder()
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
     * @param appId 应用id
     * @return AI代码生成器服务实例
     */
    public AiCodeGeneratorService getAiCodeGeneratorService(long appId) {
        return getAiCodeGeneratorService(appId, CodeGenTypeEnum.HTML);
    }

    /**
     * 根据appId和codeGenTypeEnum获取独立的AI代码生成器服务实例（带缓存）
     * @param appId 应用ID
     * @param codeGenTypeEnum 代码生成类型
     * @return AI代码生成器服务实例
     */
    public AiCodeGeneratorService getAiCodeGeneratorService(long appId, CodeGenTypeEnum codeGenTypeEnum) {
        // 根据appId构建独立的对话记忆
        // 使用serviceCache缓存服务实例，如果不存在则调用createAiCodeGeneratorService方法创建
        String cacheKey = buildCacheKey(appId, codeGenTypeEnum);
        return serviceCache.get(cacheKey, service -> createAiCodeGeneratorService(appId, codeGenTypeEnum));
    }

    /**
     * 根据appId和代码生成类型构建缓存键
     * @param appId 应用ID
     * @param codeGenTypeEnum 代码生成类型
     * @return 返回构建的缓存键
     */
    public String buildCacheKey(long appId, CodeGenTypeEnum codeGenTypeEnum) {
        return appId + "_" + codeGenTypeEnum.getValue();
    }

    /**
     * 创建AI代码生成器服务实例
     * @param appId 应用ID
     * @param codeGenTypeEnum 代码生成类型
     * @return 返回新创建的AI代码生成器服务实例
     */
    public AiCodeGeneratorService createAiCodeGeneratorService(long appId, CodeGenTypeEnum codeGenTypeEnum) {
        // 记录创建服务实例的日志
        log.info("创建AI代码生成器服务实例，appId:{}, 代码生成类型:{}", appId, codeGenTypeEnum.getValue());
        // 根据appId构建独立的对话记忆，确保每个应用有独立的上下文
        // 使用MessageWindowChatMemory创建具有固定窗口大小的对话记忆
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory
                .builder()
                .id(appId) // 设置记忆ID为appId，确保每个应用有独立的对话记忆
                .chatMemoryStore(redisChatMemoryStore) // 使用Redis作为持久化存储，实现记忆的共享和持久化
                .maxMessages(100) // 设置最大记忆消息数
                .build();
        // 从数据库加载历史对话到记忆中
        chatHistoryService.loadChatHistoryToMemory(appId, chatMemory, 20);
//        // 使用AiServices构建器模式创建AI代码生成器服务实例
//        return AiServices
//                .builder(AiCodeGeneratorService.class)
//                .chatModel(chatModel) // 配置聊天模型
//                .streamingChatModel(openAiStreamingChatModel) // 配置流式聊天模型，支持流式响应
//                .chatMemory(chatMemory) // 配置对话记忆，维护上下文
//                .build();
        // 使用 switch 语句根据 codeGenTypeEnum 的值返回不同的 AiCodeGeneratorService 实例
        return switch (codeGenTypeEnum) {
            // 当代码生成类型为VUE_PROJECT时，构建一个支持流式输出的AI代码生成服务
            case VUE_PROJECT -> AiServices.builder(AiCodeGeneratorService.class)
                    // 设置推理流式聊天模型
                    .streamingChatModel(reasoningStreamingChatModel)
                    // 设置聊天内存提供者，使用memoryId获取对应的chatMemory
                    .chatMemoryProvider(memoryId -> chatMemory)
                    // 添加文件写入工具
                    .tools(toolManager.getAllTools())
                    // 配置幻觉工具名称策略，当工具不存在时返回错误信息
                    .hallucinatedToolNameStrategy(
                            toolExecutionRequest -> ToolExecutionResultMessage.from(
                                    toolExecutionRequest, "Error: there is no tool called "
                                            + toolExecutionRequest.name()
                            )
                    )
                    // 设置最大连续工具调用次数为20
                    .maxSequentialToolsInvocations(20)
                    .build();
            // 当代码生成类型为HTML或MULTI_FILE时，构建一个基础AI代码生成服务
            case HTML, MULTI_FILE -> AiServices.builder(AiCodeGeneratorService.class)
                    // 设置聊天模型
                    .chatModel(chatModel)
                    // 设置流式聊天模型
                    .streamingChatModel(openAiStreamingChatModel)
                    // 设置聊天内存
                    .chatMemory(chatMemory)
                    .build();
            // 默认情况，抛出业务异常，表示不支持的代码生成类型
            default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR, "不支持的代码生成类型："
                    + codeGenTypeEnum.getValue());
        };
    }
}

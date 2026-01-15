package com.jjp.ai;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: jjp
 * @CreateTime: 2026-01-14
 * @Description: AI代码生成器服务工厂类
 * @Version: 1.0
 */

@Configuration
public class AiCodeGeneratorServiceFactory {

    // 注入ChatModel Bean，用于与AI模型进行交互
    @Resource
    private ChatModel chatModel;

    // 注入StreamingChatModel Bean，用于支持流式响应
    @Resource
    private StreamingChatModel streamingChatModel;

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
        return AiServices
                .builder(AiCodeGeneratorService.class)
                .chatModel(chatModel)
                .streamingChatModel(streamingChatModel)
                .build();
    }
}

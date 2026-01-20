package com.jjp.ai;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: jjp
 * @CreateTime: 2026-01-20
 * @Description: AI代码生成类型路由服务工厂
 * @Version: 1.0
 */

@Configuration
public class AiCodeGenTypeRoutingServiceFactory {

    @Resource
    private ChatModel chatModel;

    /**
     * 创建并返回一个AiCodeGenTypeRoutingService实例的工厂方法
     *
     * @return 返回一个配置好的AiCodeGenTypeRoutingService实例
     */
    @Bean
    public AiCodeGenTypeRoutingService aiCodeGenTypeRoutingService() {  // 定义工厂方法，返回AiCodeGenTypeRoutingService服务实例
        return AiServices.builder(AiCodeGenTypeRoutingService.class)  // 使用构建器模式创建服务实例，指定服务接口类型
                .chatModel(chatModel)  // 设置聊天模型参数
                .build();  // 完成构建并返回服务实例
    }
}

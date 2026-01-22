package com.jjp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: jjp
 * @CreateTime: 2026-01-22
 * @Description: 流式模型线程池配置
 * @Version: 1.0
 */

@Configuration
public class OpenAiStreamChatModelConfig {

    /**
     * 创建并配置一个名为"openAiStreamingChatModelTaskExecutor"的异步任务执行器
     * 该执行器专门用于处理OpenAI流式聊天模型的异步任务
     *
     * @return 配置好的AsyncTaskExecutor实例，用于异步执行任务
     */
    @Bean("openAiStreamingChatModelTaskExecutor")
    AsyncTaskExecutor openAiStreamingChatModelTaskExecutor() {
        // 创建线程池任务执行器实例
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 设置线程名称前缀，便于在日志和监控中识别线程来源
        taskExecutor.setThreadNamePrefix("my-LangChain4j-OpenAI-");
        // 设置核心线程池大小，即保持活跃的线程数量
        taskExecutor.setCorePoolSize(6);
        // 设置最大线程池大小，当任务队列满时可以创建的最大线程数
        taskExecutor.setMaxPoolSize(12);
        // 设置任务队列容量，用于缓冲待执行的任务
        taskExecutor.setQueueCapacity(100);
        // 设置线程空闲时间，超过此时间的空闲线程将被终止
        taskExecutor.setKeepAliveSeconds(60);
        // 设置拒绝任务时的处理策略，这里使用CallerRunsPolicy策略，
        // 表示当线程池和队列都满时，由提交任务的线程自己执行该任务
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化任务执行器
        taskExecutor.initialize();
        // 返回配置好的任务执行器
        return taskExecutor;
    }
}

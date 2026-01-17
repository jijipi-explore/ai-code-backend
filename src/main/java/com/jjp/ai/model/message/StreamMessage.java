package com.jjp.ai.model.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: jjp
 * @CreateTime: 2026-01-17
 * @Description: 流式消息响应基类
 * @Version: 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StreamMessage {
    private String type;
}

package com.jjp.model.dto.app;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: jjp
 * @CreateTime: 2026-01-15
 * @Description: 应用创建请求类
 * @Version: 1.0
 */

@Data
public class AppAddRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 应用初始化的prompt
     */
    private String initPrompt;
}

package com.jjp.model.dto.app;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: jjp
 * @CreateTime: 2026-01-15
 * @Description: 应用部署请求类
 * @Version: 1.0
 */

@Data
public class AppDeployRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 应用id
     */
    private Long appId;
}

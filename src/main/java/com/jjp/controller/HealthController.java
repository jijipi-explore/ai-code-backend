package com.jjp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: jjp
 * @CreateTime: 2026-01-12
 * @Description: 健康检查控制器
 * @Version: 1.0
 */

@RestController
@RequestMapping("/health")
public class HealthController {
    /**
     * 健康检查接口
     * 当系统正常运行时返回"ok"
     *
     * @return 返回系统健康状态，正常情况下返回"ok"
     */
    @GetMapping("/")
    public String healthCheck() {
        return "ok";
    }
}

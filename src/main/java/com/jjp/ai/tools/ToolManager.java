package com.jjp.ai.tools;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: jjp
 * @CreateTime: 2026-01-20
 * @Description: 工具管理器，统一管理所有工具，提供根据名称获取工具的功能
 * @Version: 1.0
 */

@Slf4j
@Component
public class ToolManager {

    /**
     * 工具名称到工具实例的映射
     */
    private final Map<String, BaseTool> toolMap = new HashMap<>();

    /**
     * 自动注入所有工具
     */
    @Resource
    private BaseTool[] tools;

    /**
     * 初始化工具管理器，将所有工具注册到工具名称到工具实例的映射中
     */
    @PostConstruct
    public void init() {
        for (BaseTool tool : tools) {
            toolMap.put(tool.getToolName(), tool);
            log.info("注册工具: {} -> {}", tool.getToolName(), tool.getDisplayName());
        }
        log.info("工具管理器初始化完成，共注册了 {} 个工具", toolMap.size());
    }

    /**
     * 根据工具名称获取对应的工具对象
     * @param toolName 工具名称，用于在工具映射中查找对应的工具
     * @return 返回找到的工具对象，如果未找到则返回null
     */
    public BaseTool getTool(String toolName) {
        return toolMap.get(toolName);
    }

    /**
     * 获取所有工具的方法
     * @return BaseTool[] 返回包含所有工具的数组
     */
    public BaseTool[] getAllTools() {
        return tools; // 返回存储所有工具的数组
    }
}

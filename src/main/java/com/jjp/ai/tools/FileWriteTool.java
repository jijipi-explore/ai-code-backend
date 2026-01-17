package com.jjp.ai.tools;

import com.jjp.constant.AppConstant;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolMemoryId;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @Author: jjp
 * @CreateTime: 2026-01-14
 * @Description: 支持 AI 通过工具调用的方式写入文件
 * @Version: 1.0
 */

@Slf4j
public class FileWriteTool {

    /**
     * 写入文件到指定路径
     * @param relativeFilePath 文件的相对路径
     * @param content 要写入文件的内容
     * @param appId 应用程序ID，用于创建项目目录
     * @return 返回操作结果信息，成功或失败
     */
    @Tool("写入文件到指定路径")
    public String writeFile(@P("文件的相对路径") String relativeFilePath,
                            @P("要写入文件的内容") String content, @ToolMemoryId Long appId) {
        try {
            // 将字符串路径转换为Path对象
            Path path = Paths.get(relativeFilePath);
            // 检查是否为绝对路径
            if (!path.isAbsolute()) {
                // 相对路径处理，创建基于 appId 的项目目录
                String projectDirName = "vue_project_" + appId;  // 构建项目目录名称
                Path projectRoot = Paths.get(AppConstant.CODE_OUTPUT_ROOT_DIR, projectDirName);  // 获取项目根路径
                path = projectRoot.resolve(relativeFilePath);  // 解析为完整路径
            }
            // 创建父目录（如果不存在）
            Path parentDir = path.getParent();
            if (parentDir != null) {
                Files.createDirectories(parentDir);  // 递归创建所有必要的父目录
            }
            // 写入文件内容
            Files.write(path, content.getBytes(),  // 将内容转换为字节数组写入
                    StandardOpenOption.CREATE,     // 如果文件不存在则创建
                    StandardOpenOption.TRUNCATE_EXISTING);  // 如果文件存在则截断
            log.info("成功写入文件: {}", path.toAbsolutePath());  // 记录成功日志
            // 注意要返回相对路径，不能让 AI 把文件绝对路径返回给用户
            return "文件写入成功: " + relativeFilePath;
        } catch (IOException e) {
            String errorMessage = "文件写入失败: " + relativeFilePath + ", 错误: " + e.getMessage();
            log.error(errorMessage, e);  // 记录错误日志
            return errorMessage;  // 返回错误信息
        }
    }
}

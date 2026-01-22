package com.jjp.ai.tools;

import cn.hutool.json.JSONObject;
import com.jjp.constant.AppConstant;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolMemoryId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @Author: jjp
 * @CreateTime: 2026-01-20
 * @Description: 支持 AI 通过工具调用的方式删除文件
 * @Version: 1.0
 */
@Slf4j
@Component
public class FileDeleteTool extends BaseTool {

    /**
     * 删除指定路径的文件
     * @param relativeFilePath 文件的相对路径
     * @param appId 应用ID，用于确定项目目录
     * @return 操作结果信息
     */
    @Tool("删除指定路径的文件")
    public String deleteFile(@P("文件的相对路径") String relativeFilePath, @ToolMemoryId Long appId) {
        try {
            // 将相对路径转换为Path对象
            Path path = Paths.get(relativeFilePath);
            // 如果路径不是绝对路径，则拼接完整的项目路径
            if (!path.isAbsolute()) {
                // 根据appId构建项目目录名称
                String projectDirName = "vue_project_" + appId;
                // 获取项目根目录路径
                Path projectRoot = Paths.get(AppConstant.CODE_OUTPUT_ROOT_DIR, projectDirName);
                // 解析并合并路径
                path = projectRoot.resolve(relativeFilePath);
            }
            // 检查文件是否存在
            if (!Files.exists(path)) {
                return "警告：文件不存在，无需删除 - " + relativeFilePath;
            }
            // 检查路径是否为普通文件
            if (!Files.isRegularFile(path)) {
                return "错误：指定路径不是文件，无法删除 - " + relativeFilePath;
            }
            // 安全检查：避免删除重要文件
            String fileName = path.getFileName().toString();
            if (isImportantFile(fileName)) {
                return "错误：不允许删除重要文件 - " + fileName;
            }
            // 执行文件删除操作
            Files.delete(path);
            // 记录删除成功的日志
            log.info("成功删除文件: {}", path.toAbsolutePath());
            return "文件删除成功: " + relativeFilePath;
        } catch (IOException e) {
            // 构造并返回错误信息
            String errorMessage = "删除文件失败: " + relativeFilePath + ", 错误: " + e.getMessage();
            // 记录错误日志
            log.error(errorMessage, e);
            return errorMessage;
        }
    }

    /**
     * 判断是否是重要文件，不允许删除
     */
    private boolean isImportantFile(String fileName) {
        String[] importantFiles = {
                "package.json", "package-lock.json", "yarn.lock", "pnpm-lock.yaml",
                "vite.config.js", "vite.config.ts", "vue.config.js",
                "tsconfig.json", "tsconfig.app.json", "tsconfig.node.json",
                "index.html", "main.js", "main.ts", "App.vue", ".gitignore", "README.md"
        };
        for (String important : importantFiles) {
            if (important.equalsIgnoreCase(fileName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getToolName() {
        return "deleteFile";
    }

    @Override
    public String getDisplayName() {
        return "删除文件";
    }

    @Override
    public String generateToolExecutedResult(JSONObject arguments) {
        String relativeFilePath = arguments.getStr("relativeFilePath");
        return String.format("[工具调用] %s %s", getDisplayName(), relativeFilePath);
    }
}

package com.jjp.ai.model;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

/**
 * @Author: jjp
 * @CreateTime: 2026-01-14
 * @Description: AI结构化输出，生成 HTML 代码文件的结果类
 * @Version: 1.0
 */

@Description("生成 HTML 代码文件的结果")
@Data
public class HtmlCodeResult {

    /**
     * HTML代码内容
     * 存储生成的HTML字符串
     */
    @Description("HTML代码")
    private String htmlCode;

    /**
     * 生成代码的描述信息
     * 用于说明生成的HTML代码的功能或用途
     */
    @Description("生成代码的描述")
    private String description;
}

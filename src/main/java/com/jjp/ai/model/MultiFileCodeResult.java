package com.jjp.ai.model;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

/**
 * @Author: jjp
 * @CreateTime: 2026-01-14
 * @Description: AI结构化输出，生成多个代码文件的结果类
 * @Version: 1.0
 */

@Description("生成多个代码文件的结果")
@Data
public class MultiFileCodeResult {

    /**
     * HTML代码
     * 用于存储生成的HTML标记语言代码
     * 可以包含网页的结构、内容等HTML元素
     */
    @Description("HTML代码")
    private String htmlCode;

    /**
     * CSS代码
     * 用于存储层叠样式表(CSS)代码
     * 包含对网页元素的样式定义，如布局、颜色、字体等
     */
    @Description("CSS代码")
    private String cssCode;

    /**
     * JavaScript代码
     * 用于存储JavaScript脚本代码
     * 包含网页的交互逻辑和动态功能实现
     */
    @Description("JS代码")
    private String jsCode;

    /**
     * 代码描述信息
     * 用于存储对生成代码的详细说明
     * 可以包括代码的功能说明、使用方法、注意事项等
     */
    @Description("生成代码的描述")
    private String description;
}

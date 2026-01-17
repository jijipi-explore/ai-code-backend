package com.jjp.model.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

/**
 * @Author: jjp
 * @CreateTime: 2026-01-14
 * @Description: 代码生成类型枚举类
 * @Version: 1.0
 */

@Getter
public enum CodeGenTypeEnum {

    /**
     * 原生 HTML 模式
     * text: "原生 HTML 模式"
     * value: "html"
     */
    HTML("原生 HTML 模式", "html"),
    /**
     * 原生多文件模式
     * text: "原生多文件模式"
     * value: "multi_file"
     */
    MULTI_FILE("原生多文件模式", "multi_file"),

    /**
     * Vue 工程模式
     * text: "Vue 工程模式"
     * value: "vue_project"
     */
    VUE_PROJECT("Vue 工程模式", "vue_project");

    // 枚举的文本描述
    private final String text;
    // 枚举的值
    private final String value;

    /**
     * 枚举构造函数
     * @param text 枚举的文本描述
     * @param value 枚举的值
     */
    CodeGenTypeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value 枚举值的value
     * @return 枚举值
     */
    public static CodeGenTypeEnum getEnumByValue(String value) {
        if (ObjUtil.isEmpty(value)) {
            return null;
        }
        for (CodeGenTypeEnum anEnum : CodeGenTypeEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
}

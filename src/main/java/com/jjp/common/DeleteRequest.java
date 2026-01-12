package com.jjp.common;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: jjp
 * @CreateTime: 2026-01-12
 * @Description: 通用基础响应类，用于封装API返回结果
 * @Version: 1.0
 */

@Data
public class DeleteRequest implements Serializable {

    /**
     * id属性
     * 用于标识需要删除的对象的唯一ID
     */
    private Long id; // 定义一个Long类型的私有属性id，用于存储要删除对象的ID

    @Serial
    private static final long serialVersionUID = 1L; // 序列化版本UID，用于在序列化和反序列化过程中验证版本一致性
}

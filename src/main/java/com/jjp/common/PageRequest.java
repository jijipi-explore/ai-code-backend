package com.jjp.common;

import lombok.Data;

/**
 * @Author: jjp
 * @CreateTime: 2026-01-12
 * @Description: 分页请求参数类
 * @Version: 1.0
 */

@Data
public class PageRequest {
    /**
     * 当前页号
     * 默认值为1
     */
    private int pageNum = 1;

    /**
     * 页面大小
     * 默认值为10，表示每页显示10条数据
     */
    private int pageSize = 10;

    /**
     * 排序字段
     * 用于指定按哪个字段进行排序，可以为空
     */
    private String sortField;

    /**
     * 排序顺序（默认降序）
     */
    private String sortOrder = "descend";
}

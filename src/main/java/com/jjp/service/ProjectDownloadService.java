package com.jjp.service;

import jakarta.servlet.http.HttpServletResponse;

/**
 * @Author: jjp
 * @CreateTime: 2026-01-20
 * @Description: 工程下载服务层接口
 * @Version: 1.0
 */

public interface ProjectDownloadService {
    /**
     * 下载工程为zip文件
     * @param projectPath 工程路径
     * @param downloadFileName 下载文件名
     * @param response http响应
     */
    void downloadProjectAsZip(String projectPath, String downloadFileName, HttpServletResponse response);
}

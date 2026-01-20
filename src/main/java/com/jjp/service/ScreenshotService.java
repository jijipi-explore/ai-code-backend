package com.jjp.service;

/**
 * @Author: jjp
 * @CreateTime: 2026-01-18
 * @Description: 截图服务接口
 * @Version: 1.0
 */

public interface ScreenshotService {
    /**
     * 生成并上传截图的方法
     * @param webUrl 需要截图的网页URL地址
     * @return cos对象存储中图片的url
     */
    String generateAndUploadScreenshot(String webUrl);
}

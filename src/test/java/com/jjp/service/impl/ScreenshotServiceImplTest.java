package com.jjp.service.impl;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class ScreenshotServiceImplTest {

    @Resource
    private ScreenshotServiceImpl screenshotService;

    @Test
    void generateAndUploadScreenshot() {
        String url = screenshotService.generateAndUploadScreenshot("https://www.baidu.com");
        System.out.println(url);
        Assertions.assertNotNull(url);
    }
}
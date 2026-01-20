package com.jjp.manager;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.jjp.exception.BusinessException;
import com.jjp.exception.ErrorCode;
import io.github.bonigarcia.wdm.WebDriverManager;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: jjp
 * @CreateTime: 2026-01-18
 * @Description: 网页界面截图工具
 * @Version: 1.0
 */

@Slf4j
@Component
public class WebScreenshotManager {


    /**
     * 初始化Chrome WebDriver实例，设置浏览器窗口大小为1600x900
     * 使用static final确保全局唯一且不可变
     */
    private static final WebDriver webDriver = initChromeDriver(1600, 900);
    /**
     * 创建单线程执行器，用于异步任务执行
     * 使用final确保引用不可变
     */
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();


    /**
     * 异步截取网页截图的方法
     *
     * @param webUrl 需要截取截图的网页URL
     * @return CompletableFuture<String> 异步返回截图保存路径
     */
    public CompletableFuture<String> takeScreenshotAsync(String webUrl) {
        return CompletableFuture.supplyAsync(
                () -> saveWebPageScreenshot(webUrl), executorService
        );
    }

    /**
     * 生成网页截图
     *
     * @param webUrl 网页URL
     * @return 压缩后的截图文件路径，失败返回null
     */
    public static String saveWebPageScreenshot(String webUrl) {
        if (StrUtil.isBlank(webUrl)) {
            log.error("网页URL不能为空");
            return null;
        }
        try {
            // 创建临时目录
            String rootPath = System.getProperty("user.dir") + File.separator + "tmp" + File.separator + "screenshots"
                    + File.separator + UUID.randomUUID().toString().substring(0, 8);
            FileUtil.mkdir(rootPath);
            // 图片后缀
            final String IMAGE_SUFFIX = ".jpg";
            // 原始截图文件路径
            String imageSavePath = rootPath + File.separator + RandomUtil.randomString(6) + IMAGE_SUFFIX;
            // 访问网页
            webDriver.get(webUrl);
            // 等待页面加载完成
            waitForPageLoad(webDriver);
            // 截图
            byte[] screenshotBytes = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES);
            // 保存原始图片
            saveImage(screenshotBytes, imageSavePath);
            log.info("原始截图保存成功: {}", imageSavePath);
            // 压缩图片
            final String COMPRESSION_SUFFIX = "_compressed.jpg";
            String compressedImagePath = rootPath + File.separator + RandomUtil.randomString(6) + COMPRESSION_SUFFIX;
            compressImage(imageSavePath, compressedImagePath);
            log.info("压缩图片保存成功: {}", compressedImagePath);
            // 删除原始图片，只保留压缩图片
            FileUtil.del(imageSavePath);
            return compressedImagePath;
        } catch (Exception e) {
            log.error("网页截图失败: {}", webUrl, e);
            return null;
        }
    }

    /**
     * 保存图片到指定路径
     * @param imageBytes 图片字节数组
     * @param imagePath 图片保存路径
     */
    private static void saveImage(byte[] imageBytes, String imagePath) {
        try {
            // 使用FileUtil工具类将字节数组写入指定路径
            FileUtil.writeBytes(imageBytes, imagePath);
        } catch (Exception e) {
            // 记录错误日志，包含图片路径和异常信息
            log.error("保存图片失败: {}", imagePath, e);
            // 抛出业务异常，提示保存图片失败
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "保存图片失败");
        }
    }

    /**
     * 压缩图片的方法
     * @param originalImagePath 原始图片路径
     * @param compressedImagePath 压缩后图片保存路径
     */
    private static void compressImage(String originalImagePath, String compressedImagePath) {
        // 压缩图片质量（0.1 = 10% 质量）
        final float COMPRESSION_QUALITY = 0.3f;
        try {
            // 使用ImgUtil工具类进行图片压缩
            // 原始图片文件路径转换为File对象
            // 压缩后图片保存路径转换为File对象
            // 设置压缩质量为30%
            ImgUtil.compress(
                    FileUtil.file(originalImagePath),
                    FileUtil.file(compressedImagePath),
                    COMPRESSION_QUALITY
            );
        } catch (Exception e) {
            // 记录压缩失败的错误日志，包含原始路径和目标路径以及异常信息
            log.error("压缩图片失败: {} -> {}", originalImagePath, compressedImagePath, e);
            // 抛出业务异常，提示用户压缩图片失败
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "压缩图片失败");
        }
    }

    /**
     * 等待页面完全加载的方法
     * 该方法会等待页面加载完成，包括动态内容
     * @param driver WebDriver实例，用于控制浏览器
     */
    private static void waitForPageLoad(WebDriver driver) {
        try {
            // 创建等待页面加载对象，最长等待10秒
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            // 等待 document.readyState 为complete，表示页面加载完成
            wait.until(webDriver ->
                    ((JavascriptExecutor) webDriver).executeScript("return document.readyState")
                            .equals("complete")
            );
            // 额外等待一段时间，确保动态内容加载完成
            Thread.sleep(2000);
            log.info("页面加载完成");
        } catch (Exception e) {
            log.error("等待页面加载时出现异常，继续执行截图", e);
        }
    }

    /**
     * 销毁方法，用于在Bean被销毁前执行资源清理操作
     * 当Spring容器销毁时，此方法会被自动调用
     * 通过@PreDestroy注解标记为生命周期回调方法
     */
    @PreDestroy    // 标记为Bean销毁前的回调方法
    public void destroy() {    // 定义销毁方法
        executorService.shutdown(); // 关闭线程池
        webDriver.quit();    // 退出WebDriver，关闭所有相关窗口并释放资源
    }

    /**
     * 初始化 Chrome 浏览器驱动
     */
    private static WebDriver initChromeDriver(int width, int height) {
        try {
            // 自动管理 ChromeDriver
            WebDriverManager.chromedriver().setup();
            // 配置 Chrome 选项
            ChromeOptions options = new ChromeOptions();
            // 无头模式
            options.addArguments("--headless");
            // 禁用GPU（在某些环境下避免问题）
            options.addArguments("--disable-gpu");
            // 禁用沙盒模式（Docker环境需要）
            options.addArguments("--no-sandbox");
            // 禁用开发者shm使用
            options.addArguments("--disable-dev-shm-usage");
            // 设置窗口大小
            options.addArguments(String.format("--window-size=%d,%d", width, height));
            // 禁用扩展
            options.addArguments("--disable-extensions");
            // 设置用户代理
            options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
            // 创建驱动
            WebDriver driver = new ChromeDriver(options);
            // 设置页面加载超时
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            // 设置隐式等待
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            return driver;
        } catch (Exception e) {
            log.error("初始化 Chrome 浏览器失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "初始化 Chrome 浏览器失败");
        }
    }
}

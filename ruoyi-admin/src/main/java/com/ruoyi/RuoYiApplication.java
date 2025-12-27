package com.ruoyi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

/**
 * 启动程序
 * 
 * @author ruoyi
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class RuoYiApplication {
    /** 启动开始时间 */
    private static long startTime = System.currentTimeMillis();

    /** 日志对象 */
    private static final Logger log = LoggerFactory.getLogger(RuoYiApplication.class);

    public static void main(String[] args) {
        // 记录启动开始时间
        startTime = System.currentTimeMillis();
        log.info("===========================================");
        log.info("社团管理系统正在启动...");
        log.info("===========================================");

        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication application = new SpringApplication(RuoYiApplication.class);

        // Spring Boot 3.0 性能优化配置
        application.setBannerMode(Banner.Mode.OFF); // 关闭横幅输出，减少启动时间
        application.setRegisterShutdownHook(true); // 启用优雅关闭
        application.setLogStartupInfo(false); // 关闭启动信息日志，减少冗余输出

        // 添加应用启动完成监听器
        application.addListeners((ApplicationListener<ApplicationReadyEvent>) event -> {
            // 计算启动耗时
            long endTime = System.currentTimeMillis();
            long costTime = endTime - startTime;

            // 输出启动成功信息
            System.out.println("(♥◠‿◠)ﾉﾞ  社团管理系统启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                    "    ___     _____  _____  ____   ______   ____     ___     ______   ____    ____      _   __\n" +
                    "   /   |   / ___/ / ___/ / __ \\ / ____/  /  _/    /   |   /_  __/  /  _/   / __ \\    / | / /\n" +
                    "  / /| |   \\__ \\  \\__ \\ / / / // /       / /     / /| |    / /     / /    / / / /   /  |/ / \n"
                    +
                    " / ___ |  ___/ / ___/ // /_/ // /___   _/ /     / ___ |   / /    _/ /    / /_/ /   / /|  /  \n" +
                    "/_/  |_| /____/ /____/ \\____/ \\____/  /___/    /_/  |_|  /_/    /___/    \\____/   /_/ |_/   \n");

            // 输出启动日志
            log.info("===========================================");
            log.info("社团管理系统启动成功！");
            log.info("本次项目启动花费时间: {} 毫秒 ({} 秒)", costTime, String.format("%.2f", costTime / 1000.0));
            log.info("===========================================");
        });

        application.run(args);
    }
}

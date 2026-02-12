package com.ruoyi.quartz.task;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.ruoyi.user.service.IClubActivityService;

/**
 * 社团活动定时任务
 * 项目启动时执行一次，之后每1小时检查一次活动状态
 */
@Component("clubActivityTask")
public class ClubActivityTask {

    private static final Logger log = LoggerFactory.getLogger(ClubActivityTask.class);

    @Autowired
    private IClubActivityService clubActivityService;

    /**
     * 项目启动时立即执行一次
     */
    @PostConstruct
    public void init() {
        log.info("项目启动 - 执行活动状态更新...");
        updateActivityStatus();
    }

    /**
     * 每1小时自动更新活动状态
     * 0=待开始, 1=进行中, 2=已结束
     * 根据当前时间与 start_time, end_time 对比更新
     */
    @Scheduled(fixedRate = 3600000)
    public void updateActivityStatus() {
        clubActivityService.updateActivityStatusBasedOnTime();
        log.info("定时任务执行：更新社团活动状态完成");
    }
}

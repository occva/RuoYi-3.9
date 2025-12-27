package com.ruoyi.quartz.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ruoyi.user.service.IClubActivityService;

/**
 * 社团活动定时任务
 * 
 * @author ruoyi
 */
@Component("clubActivityTask")
public class ClubActivityTask {
    @Autowired
    private IClubActivityService clubActivityService;

    /**
     * 自动更新活动状态
     * 0=即将开始, 1=进行中, 2=已结束
     * 根据当前时间与 start_time, end_time 对比更新
     */
    public void updateActivityStatus() {
        clubActivityService.updateActivityStatusBasedOnTime();
        // System.out.println("定时任务执行：更新社团活动状态完成");
    }
}

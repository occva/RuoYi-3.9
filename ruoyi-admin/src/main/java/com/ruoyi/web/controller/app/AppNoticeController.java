package com.ruoyi.web.controller.app;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.user.domain.ClubNotice;
import com.ruoyi.user.service.IClubNoticeService;

/**
 * 社团公告Controller (用户端 - 只读)
 * 
 * @author ruoyi
 */
@Anonymous
@RestController
@RequestMapping("/api/app/notice")
public class AppNoticeController extends BaseController {

    @Autowired
    private IClubNoticeService clubNoticeService;

    /**
     * 获取公告列表（分页）
     */
    @GetMapping("/list")
    public TableDataInfo list(ClubNotice notice) {
        // 只查询已发布的公告
        notice.setStatus("1");
        notice.setDelFlag("0");
        startPage();
        List<ClubNotice> list = clubNoticeService.selectClubNoticeList(notice);
        return getDataTable(list);
    }

    /**
     * 获取社团的公告列表
     */
    @GetMapping("/club/{clubId}")
    public AjaxResult listByClub(@PathVariable Long clubId) {
        ClubNotice query = new ClubNotice();
        query.setClubId(clubId);
        query.setStatus("1"); // 只查询已发布
        query.setDelFlag("0");
        List<ClubNotice> list = clubNoticeService.selectClubNoticeList(query);
        return success(list);
    }

    /**
     * 获取公告详情
     */
    @GetMapping("/{noticeId}")
    public AjaxResult getInfo(@PathVariable Long noticeId) {
        ClubNotice notice = clubNoticeService.selectClubNoticeById(noticeId);
        if (notice == null || !"1".equals(notice.getStatus())) {
            return error("公告不存在或未发布");
        }
        return success(notice);
    }
}

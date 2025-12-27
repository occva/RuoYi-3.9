package com.ruoyi.user.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.user.domain.ClubNotice;
import com.ruoyi.user.service.IClubNoticeService;

/**
 * 社团公告Controller (用户端)
 */
@Anonymous
@RestController
@RequestMapping("/api/user/notice")
public class ClubNoticeController extends BaseController {
    
    @Autowired
    private IClubNoticeService clubNoticeService;

    /**
     * 获取公告列表
     */
    @GetMapping("/list")
    public AjaxResult list(ClubNotice notice) {
        List<ClubNotice> list = clubNoticeService.selectClubNoticeList(notice);
        return success(list);
    }

    /**
     * 获取社团的公告列表
     */
    @GetMapping("/club/{clubId}")
    public AjaxResult listByClub(@PathVariable Long clubId) {
        return success(clubNoticeService.selectNoticeByClubId(clubId));
    }

    /**
     * 获取公告详情
     */
    @GetMapping("/{noticeId}")
    public AjaxResult getInfo(@PathVariable Long noticeId) {
        return success(clubNoticeService.selectClubNoticeById(noticeId));
    }
}

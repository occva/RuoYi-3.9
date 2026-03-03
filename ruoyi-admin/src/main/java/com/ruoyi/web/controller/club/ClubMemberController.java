package com.ruoyi.web.controller.club;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.user.domain.ClubMember;
import com.ruoyi.user.service.IClubMemberService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 社团成员Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/member")
public class ClubMemberController extends BaseController {
    @Autowired
    private IClubMemberService clubMemberService;

    /**
     * 查询社团成员列表
     */
    @PreAuthorize("@ss.hasPermi('club:member:list')")
    @GetMapping("/list")
    public TableDataInfo list(ClubMember clubMember) {
        startPage();
        List<ClubMember> list = clubMemberService.selectClubMemberList(clubMember);
        return getDataTable(list);
    }

    /**
     * 导出社团成员列表
     */
    @PreAuthorize("@ss.hasPermi('club:member:export')")
    @Log(title = "社团成员", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ClubMember clubMember) {
        List<ClubMember> list = clubMemberService.selectClubMemberList(clubMember);
        ExcelUtil<ClubMember> util = new ExcelUtil<ClubMember>(ClubMember.class);
        util.exportExcel(response, list, "社团成员数据");
    }

    /**
     * 获取社团成员详细信息
     */
    @PreAuthorize("@ss.hasPermi('club:member:query')")
    @GetMapping(value = "/{memberId}")
    public AjaxResult getInfo(@PathVariable("memberId") Long memberId) {
        return success(clubMemberService.selectClubMemberById(memberId));
    }

    /**
     * 新增社团成员
     */
    @PreAuthorize("@ss.hasPermi('club:member:add')")
    @Log(title = "社团成员", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ClubMember clubMember) {
        return toAjax(clubMemberService.insertClubMember(clubMember));
    }

    /**
     * 修改社团成员
     */
    @PreAuthorize("@ss.hasPermi('club:member:edit')")
    @Log(title = "社团成员", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ClubMember clubMember) {
        return toAjax(clubMemberService.updateClubMember(clubMember));
    }

    /**
     * 转让社长
     */
    @PreAuthorize("@ss.hasPermi('club:member:edit')")
    @Log(title = "社团成员", businessType = BusinessType.UPDATE)
    @PutMapping("/transfer-president")
    public AjaxResult transferPresident(@RequestBody TransferPresidentBody body) {
        return toAjax(
                clubMemberService.transferPresident(body.getClubId(), body.getFromMemberId(), body.getToMemberId(),
                        body.getFromPresidentNewRoleType()));
    }

    /**
     * 任命副社长
     */
    @PreAuthorize("@ss.hasPermi('club:member:edit')")
    @Log(title = "社团成员", businessType = BusinessType.UPDATE)
    @PutMapping("/appoint-vice-president")
    public AjaxResult appointVicePresident(@RequestBody VicePresidentBody body) {
        return toAjax(clubMemberService.appointVicePresident(body.getClubId(), body.getMemberId()));
    }

    /**
     * 撤销副社长
     */
    @PreAuthorize("@ss.hasPermi('club:member:edit')")
    @Log(title = "社团成员", businessType = BusinessType.UPDATE)
    @PutMapping("/revoke-vice-president")
    public AjaxResult revokeVicePresident(@RequestBody RevokeVicePresidentBody body) {
        return toAjax(clubMemberService.revokeVicePresident(body.getClubId(), body.getMemberId(), body.getToRoleType()));
    }

    /**
     * 删除社团成员
     */
    @PreAuthorize("@ss.hasPermi('club:member:remove')")
    @Log(title = "社团成员", businessType = BusinessType.DELETE)
    @DeleteMapping("/{memberIds}")
    public AjaxResult remove(@PathVariable Long[] memberIds) {
        return toAjax(clubMemberService.deleteClubMemberByIds(memberIds));
    }

    /**
     * 获取成员统计数据
     */
    @PreAuthorize("@ss.hasAnyPermi('system:statistics:member,club:member:list')")
    @GetMapping("/stat")
    public AjaxResult stat(@org.springframework.web.bind.annotation.RequestParam(required = false) String beginTime,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String endTime) {
        return success(clubMemberService.getStatData(beginTime, endTime));
    }

    public static class TransferPresidentBody {
        private Long clubId;
        private Long fromMemberId;
        private Long toMemberId;
        private String fromPresidentNewRoleType;

        public Long getClubId() {
            return clubId;
        }

        public void setClubId(Long clubId) {
            this.clubId = clubId;
        }

        public Long getFromMemberId() {
            return fromMemberId;
        }

        public void setFromMemberId(Long fromMemberId) {
            this.fromMemberId = fromMemberId;
        }

        public Long getToMemberId() {
            return toMemberId;
        }

        public void setToMemberId(Long toMemberId) {
            this.toMemberId = toMemberId;
        }

        public String getFromPresidentNewRoleType() {
            return fromPresidentNewRoleType;
        }

        public void setFromPresidentNewRoleType(String fromPresidentNewRoleType) {
            this.fromPresidentNewRoleType = fromPresidentNewRoleType;
        }
    }

    public static class VicePresidentBody {
        private Long clubId;
        private Long memberId;

        public Long getClubId() {
            return clubId;
        }

        public void setClubId(Long clubId) {
            this.clubId = clubId;
        }

        public Long getMemberId() {
            return memberId;
        }

        public void setMemberId(Long memberId) {
            this.memberId = memberId;
        }
    }

    public static class RevokeVicePresidentBody extends VicePresidentBody {
        private String toRoleType;

        public String getToRoleType() {
            return toRoleType;
        }

        public void setToRoleType(String toRoleType) {
            this.toRoleType = toRoleType;
        }
    }
}

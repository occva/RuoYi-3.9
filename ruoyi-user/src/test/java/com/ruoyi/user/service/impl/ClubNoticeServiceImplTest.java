package com.ruoyi.user.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.user.domain.ClubNotice;
import com.ruoyi.user.mapper.ClubNoticeMapper;

@ExtendWith(MockitoExtension.class)
class ClubNoticeServiceImplTest {

    @Mock
    private ClubNoticeMapper clubNoticeMapper;

    @Mock
    private ClubDataScopeHelper dataScopeHelper;

    @InjectMocks
    private ClubNoticeServiceImpl clubNoticeService;

    @Test
    void selectClubNoticeByIdShouldRejectOutOfScopeNotice() {
        ClubNotice notice = new ClubNotice();
        notice.setClubId(222L);

        when(clubNoticeMapper.selectClubNoticeById(1L)).thenReturn(notice);
        when(dataScopeHelper.isManagedClub(222L)).thenReturn(false);

        assertThrows(ServiceException.class, () -> clubNoticeService.selectClubNoticeById(1L));
    }
}

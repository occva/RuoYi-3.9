package com.ruoyi.user.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.user.domain.ClubApplication;
import com.ruoyi.user.mapper.ClubApplicationMapper;
import com.ruoyi.user.mapper.ClubMemberMapper;

@ExtendWith(MockitoExtension.class)
class ClubApplicationServiceImplTest {

    @Mock
    private ClubApplicationMapper clubApplicationMapper;

    @Mock
    private ClubMemberMapper clubMemberMapper;

    @Mock
    private ClubDataScopeHelper dataScopeHelper;

    @InjectMocks
    private ClubApplicationServiceImpl clubApplicationService;

    @Test
    void reviewApplicationShouldRejectOutOfScopeApplication() {
        ClubApplication application = new ClubApplication();
        application.setApplicationId(1L);
        application.setClubId(66L);

        when(clubApplicationMapper.selectClubApplicationById(1L)).thenReturn(application);
        when(dataScopeHelper.isManagedClub(66L)).thenReturn(false);

        assertThrows(ServiceException.class, () -> clubApplicationService.reviewApplication(application));
        verify(clubApplicationMapper, never()).updateClubApplication(application);
    }

    @Test
    void deleteClubApplicationByIdsShouldRejectUnauthorizedApplications() {
        ClubApplication application = new ClubApplication();
        application.setClubId(77L);

        when(dataScopeHelper.getManagedClubIds()).thenReturn(List.of(10L));
        when(clubApplicationMapper.selectClubApplicationById(1L)).thenReturn(application);

        assertThrows(ServiceException.class, () -> clubApplicationService.deleteClubApplicationByIds(new Long[] { 1L }));
        verify(clubApplicationMapper, never()).deleteClubApplicationByIds(org.mockito.ArgumentMatchers.any());
    }
}

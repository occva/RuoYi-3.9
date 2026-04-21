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
import com.ruoyi.user.domain.ClubActivity;
import com.ruoyi.user.mapper.ClubActivityMapper;

@ExtendWith(MockitoExtension.class)
class ClubActivityServiceImplTest {

    @Mock
    private ClubActivityMapper clubActivityMapper;

    @Mock
    private ClubDataScopeHelper dataScopeHelper;

    @InjectMocks
    private ClubActivityServiceImpl clubActivityService;

    @Test
    void insertClubActivityShouldRejectOutOfScopeClub() {
        ClubActivity activity = new ClubActivity();
        activity.setClubId(200L);

        when(dataScopeHelper.isManagedClub(200L)).thenReturn(false);

        assertThrows(ServiceException.class, () -> clubActivityService.insertClubActivity(activity));
        verify(clubActivityMapper, never()).insertClubActivity(activity);
    }

    @Test
    void deleteClubActivityByIdsShouldRejectUnauthorizedActivity() {
        ClubActivity activity = new ClubActivity();
        activity.setClubId(300L);

        when(dataScopeHelper.getManagedClubIds()).thenReturn(List.of(100L));
        when(clubActivityMapper.selectClubActivityById(1L)).thenReturn(activity);

        assertThrows(ServiceException.class, () -> clubActivityService.deleteClubActivityByIds(new Long[] { 1L }));
        verify(clubActivityMapper, never()).deleteClubActivityByIds(org.mockito.ArgumentMatchers.any());
    }
}

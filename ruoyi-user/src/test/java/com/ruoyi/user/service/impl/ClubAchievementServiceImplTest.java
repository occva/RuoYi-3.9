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
import com.ruoyi.user.domain.ClubAchievement;
import com.ruoyi.user.mapper.ClubAchievementMapper;

@ExtendWith(MockitoExtension.class)
class ClubAchievementServiceImplTest {

    @Mock
    private ClubAchievementMapper clubAchievementMapper;

    @Mock
    private ClubDataScopeHelper dataScopeHelper;

    @InjectMocks
    private ClubAchievementServiceImpl clubAchievementService;

    @Test
    void deleteClubAchievementByIdsShouldRejectUnauthorizedAchievement() {
        ClubAchievement achievement = new ClubAchievement();
        achievement.setClubId(300L);

        when(dataScopeHelper.getManagedClubIds()).thenReturn(List.of(100L));
        when(clubAchievementMapper.selectClubAchievementById(1L)).thenReturn(achievement);

        assertThrows(ServiceException.class, () -> clubAchievementService.deleteClubAchievementByIds(new Long[] { 1L }));
        verify(clubAchievementMapper, never()).deleteClubAchievementByIds(org.mockito.ArgumentMatchers.any());
    }
}

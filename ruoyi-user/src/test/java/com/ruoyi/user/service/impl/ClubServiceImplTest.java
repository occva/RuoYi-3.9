package com.ruoyi.user.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.user.domain.Club;
import com.ruoyi.user.mapper.ClubMapper;

@ExtendWith(MockitoExtension.class)
class ClubServiceImplTest {

    @Mock
    private ClubMapper clubMapper;

    @Mock
    private ClubDataScopeHelper dataScopeHelper;

    @InjectMocks
    private ClubServiceImpl clubService;

    @Test
    void selectClubByIdShouldRejectOutOfScopeClub() {
        Club club = new Club();
        club.setClubId(88L);

        when(clubMapper.selectClubById(88L)).thenReturn(club);
        when(dataScopeHelper.isManagedClub(88L)).thenReturn(false);

        assertThrows(ServiceException.class, () -> clubService.selectClubById(88L));
    }

    @Test
    void getStatDataShouldPassManagedClubIdsToMapper() {
        List<Long> managedClubIds = List.of(9L);
        Map<String, Object> emptyMap = Map.of();

        when(dataScopeHelper.getManagedClubIds()).thenReturn(managedClubIds);
        when(clubMapper.selectClubStatusStat("2026-04-01", "2026-04-30", managedClubIds)).thenReturn(List.of());
        when(clubMapper.selectClubRecruitingCount("2026-04-01", "2026-04-30", managedClubIds)).thenReturn(0);
        when(clubMapper.selectClubTodayStats(managedClubIds)).thenReturn(emptyMap);
        when(clubMapper.selectClubTrendStat(managedClubIds)).thenReturn(List.of());
        when(clubMapper.selectClubCategoryStat(managedClubIds)).thenReturn(List.of());
        when(clubMapper.selectClubMemberRanking(managedClubIds)).thenReturn(List.of());

        Map<String, Object> result = clubService.getStatData("2026-04-01", "2026-04-30");

        assertEquals(emptyMap, result.get("todayStats"));
        verify(clubMapper).selectClubStatusStat("2026-04-01", "2026-04-30", managedClubIds);
        verify(clubMapper).selectClubRecruitingCount("2026-04-01", "2026-04-30", managedClubIds);
        verify(clubMapper).selectClubTodayStats(managedClubIds);
        verify(clubMapper).selectClubTrendStat(managedClubIds);
        verify(clubMapper).selectClubCategoryStat(managedClubIds);
        verify(clubMapper).selectClubMemberRanking(managedClubIds);
    }
}

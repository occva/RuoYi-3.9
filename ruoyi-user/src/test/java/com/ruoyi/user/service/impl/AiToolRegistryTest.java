package com.ruoyi.user.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ruoyi.user.domain.Club;
import com.ruoyi.user.domain.ClubActivity;
import com.ruoyi.user.service.IClubActivityService;
import com.ruoyi.user.service.IClubApplicationService;
import com.ruoyi.user.service.IClubCreateApplicationService;
import com.ruoyi.user.service.IClubFavoriteService;
import com.ruoyi.user.service.IClubService;
import com.ruoyi.user.service.impl.AiToolRegistry.ToolExecution;

@ExtendWith(MockitoExtension.class)
class AiToolRegistryTest {

    @Mock
    private IClubService clubService;

    @Mock
    private IClubActivityService activityService;

    @Mock
    private IClubFavoriteService favoriteService;

    @Mock
    private IClubApplicationService applicationService;

    @Mock
    private IClubCreateApplicationService clubCreateApplicationService;

    @InjectMocks
    private AiToolRegistry aiToolRegistry;

    @Test
    void listActivitiesShouldPreferUserMessageStatusAndDropFilterOnlyKeyword() {
        when(activityService.selectClubActivityList(any(ClubActivity.class))).thenReturn(List.of());

        aiToolRegistry.execute("list_activities",
                "{\"status\":\"0\",\"keyword\":\"当前进行中的活动有哪些？\",\"limit\":20}",
                null,
                "当前进行中的活动有哪些？");

        ArgumentCaptor<ClubActivity> captor = ArgumentCaptor.forClass(ClubActivity.class);
        verify(activityService).selectClubActivityList(captor.capture());
        ClubActivity query = captor.getValue();

        assertEquals("1", query.getStatus());
        assertEquals(null, query.getActivityTitle());
    }

    @ParameterizedTest
    @MethodSource("randomActivityQuestions")
    void listActivitiesShouldNormalizeRandomNaturalLanguageQuestions(String question, String modelArgs,
            String expectedStatus) {
        when(activityService.selectClubActivityList(any(ClubActivity.class))).thenReturn(List.of());

        aiToolRegistry.execute("list_activities", modelArgs, null, question);

        ArgumentCaptor<ClubActivity> captor = ArgumentCaptor.forClass(ClubActivity.class);
        verify(activityService).selectClubActivityList(captor.capture());
        ClubActivity query = captor.getValue();

        assertEquals(expectedStatus, query.getStatus());
        assertEquals(null, query.getActivityTitle());
        assertEquals(null, query.getClubId());
    }

    static Stream<Arguments> randomActivityQuestions() {
        return Stream.of(
                Arguments.of("上个月办完的活动给我看看",
                        "{\"status\":\"success\",\"keyword\":\"上个月办完的活动给我看看\",\"clubId\":1872600000000000001}",
                        "2"),
                Arguments.of("现在还在进行的项目有哪几个",
                        "{\"status\":\"0\",\"keyword\":\"现在还在进行的项目有哪几个\"}",
                        "1"),
                Arguments.of("帮我查一下最近完成的活动",
                        "{\"status\":\"pending\",\"keyword\":\"帮我查一下最近完成的活动\",\"clubId\":0}",
                        "2"),
                Arguments.of("有没有已经黄了或者撤掉的活动",
                        "{\"status\":\"ongoing\",\"keyword\":\"有没有已经黄了或者撤掉的活动\"}",
                        "3"),
                Arguments.of("我想看看接下来能报名参加的东西",
                        "{\"status\":\"finished\",\"keyword\":\"我想看看接下来能报名参加的东西\"}",
                        "0"));
    }

    @Test
    void listActivitiesShouldNormalizeEndedStatusAndDropAllActivityNoise() {
        when(activityService.selectClubActivityList(any(ClubActivity.class))).thenReturn(List.of());

        aiToolRegistry.execute("list_activities",
                "{\"status\":\"success\",\"keyword\":\"近期所有结束活动\",\"clubId\":1872600000000000001}",
                null,
                "近期所有结束活动");

        ArgumentCaptor<ClubActivity> captor = ArgumentCaptor.forClass(ClubActivity.class);
        verify(activityService).selectClubActivityList(captor.capture());
        ClubActivity query = captor.getValue();

        assertEquals("2", query.getStatus());
        assertEquals(null, query.getActivityTitle());
        assertEquals(null, query.getClubId());
    }

    @Test
    void listActivitiesShouldDropModelClubIdForGenericClubActivityQuestion() {
        when(clubService.selectClubList(any(Club.class))).thenReturn(List.of());
        when(activityService.selectClubActivityList(any(ClubActivity.class))).thenReturn(List.of());

        aiToolRegistry.execute("list_activities",
                "{\"keyword\":\"所有社团活动\",\"clubId\":1872600000000000001}",
                null,
                "所有社团最近有什么活动？");

        ArgumentCaptor<ClubActivity> captor = ArgumentCaptor.forClass(ClubActivity.class);
        verify(activityService).selectClubActivityList(captor.capture());

        assertEquals(null, captor.getValue().getClubId());
    }

    @Test
    void listActivitiesShouldKeepClubIdWhenUserMentionsKnownClubName() {
        Club club = new Club();
        club.setClubName("编程社");
        when(clubService.selectClubList(any(Club.class))).thenReturn(List.of(club));
        when(activityService.selectClubActivityList(any(ClubActivity.class))).thenReturn(List.of());

        aiToolRegistry.execute("list_activities",
                "{\"clubId\":100}",
                null,
                "编程社最近有什么活动？");

        ArgumentCaptor<ClubActivity> captor = ArgumentCaptor.forClass(ClubActivity.class);
        verify(activityService).selectClubActivityList(captor.capture());

        assertEquals(100L, captor.getValue().getClubId());
    }

    @Test
    void listClubsShouldDropInvalidCategoryAndFilterOnlyKeyword() {
        when(clubService.selectClubList(any(Club.class))).thenReturn(List.of());

        aiToolRegistry.execute("list_clubs",
                "{\"categoryId\":0,\"keyword\":\"有哪些社团？\",\"limit\":20}",
                null,
                "有哪些社团？");

        ArgumentCaptor<Club> captor = ArgumentCaptor.forClass(Club.class);
        verify(clubService).selectClubList(captor.capture());
        Club query = captor.getValue();

        assertEquals("0", query.getStatus());
        assertEquals(null, query.getCategoryId());
        assertEquals(null, query.getClubName());
    }

    @ParameterizedTest
    @MethodSource("randomClubQuestions")
    void listClubsShouldNormalizeRandomNaturalLanguageQuestions(String question, String modelArgs,
            String expectedRecruiting) {
        when(clubService.selectClubList(any(Club.class))).thenReturn(List.of());

        aiToolRegistry.execute("list_clubs", modelArgs, null, question);

        ArgumentCaptor<Club> captor = ArgumentCaptor.forClass(Club.class);
        verify(clubService).selectClubList(captor.capture());
        Club query = captor.getValue();

        assertEquals("0", query.getStatus());
        assertEquals(null, query.getCategoryId());
        assertEquals(null, query.getClubName());
        assertEquals(expectedRecruiting, query.getIsRecruiting());
    }

    static Stream<Arguments> randomClubQuestions() {
        return Stream.of(
                Arguments.of("学校里现在都有什么组织",
                        "{\"categoryId\":0,\"keyword\":\"学校里现在都有什么组织\",\"limit\":20}",
                        null),
                Arguments.of("把能看的组织都列一下",
                        "{\"categoryId\":0,\"keyword\":\"把能看的组织都列一下\"}",
                        null),
                Arguments.of("我想找现在还收人的组织",
                        "{\"keyword\":\"我想找现在还收人的组织\"}",
                        "1"),
                Arguments.of("有没有现在还能进的团队",
                        "{\"keyword\":\"有没有现在还能进的团队\",\"categoryId\":0}",
                        "1"),
                Arguments.of("给新生随便挑几个靠谱的组织看看",
                        "{\"keyword\":\"给新生随便挑几个靠谱的组织看看\",\"categoryId\":0}",
                        null));
    }

    @ParameterizedTest
    @MethodSource("randomPersonalToolQuestions")
    void privateToolsShouldIgnoreModelUserIdentifiersForRandomNaturalLanguageQuestions(String toolName, String question) {
        ToolExecution execution = aiToolRegistry.execute(toolName,
                "{\"userId\":1,\"user_id\":1,\"applicantUserId\":1,\"reviewerId\":1,\"adminUserId\":1}",
                null,
                question);

        assertTrue(execution.getResult().getBooleanValue("ok") == false);
        assertTrue(execution.getResult().getString("message").contains("请先登录"));
        assertEquals("无参数", execution.getTrace().getArgsSummary());
    }

    static Stream<Arguments> randomPersonalToolQuestions() {
        return Stream.of(
                Arguments.of("list_my_clubs", "我现在挂在哪些组织下面"),
                Arguments.of("list_my_favorites", "之前点星星的那些还在吗"),
                Arguments.of("list_my_applications", "我投出去的申请审核到哪一步了"),
                Arguments.of("list_my_registered_activities", "我占过名额的活动帮我翻一下"));
    }

    @Test
    void getClubDetailShouldReadByIdOnly() {
        Club club = new Club();
        club.setClubId(100L);
        club.setClubName("编程魔法师");
        club.setStatus("0");
        club.setDelFlag("0");
        when(clubService.selectClubById(100L)).thenReturn(club);

        ToolExecution execution = aiToolRegistry.execute("get_club_detail",
                "{\"clubId\":100,\"keyword\":\"全部社团\"}",
                null,
                "查看编程魔法师详情");

        assertTrue(execution.getResult().getBooleanValue("ok"));
        verify(clubService).selectClubById(100L);
    }

    @Test
    void getActivityDetailShouldReadByIdOnly() {
        ClubActivity activity = new ClubActivity();
        activity.setActivityId(200L);
        activity.setActivityTitle("React 进阶工作坊");
        activity.setStatus("1");
        activity.setDelFlag("0");
        when(activityService.selectClubActivityById(200L)).thenReturn(activity);

        ToolExecution execution = aiToolRegistry.execute("get_activity_detail",
                "{\"activityId\":200,\"status\":\"success\",\"keyword\":\"近期结束活动\"}",
                null,
                "查看 React 进阶工作坊详情");

        assertTrue(execution.getResult().getBooleanValue("ok"));
        verify(activityService).selectClubActivityById(200L);
    }

    @Test
    void privateToolsShouldNotTrustModelUserIdWhenGuest() {
        ToolExecution execution = aiToolRegistry.execute("list_my_registered_activities",
                "{\"userId\":1,\"applicantUserId\":1,\"user_id\":1}",
                null,
                "我报名了哪些活动？");

        assertTrue(execution.getResult().getBooleanValue("ok") == false);
        assertTrue(execution.getResult().getString("message").contains("请先登录"));
        assertEquals("无参数", execution.getTrace().getArgsSummary());
        verify(activityService, never()).selectMyRegisteredActivities(any());
    }
}

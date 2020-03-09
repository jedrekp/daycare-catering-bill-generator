package jedrekp.daycarecateringbillgenerator.service;

import jedrekp.daycarecateringbillgenerator.DTO.response.DailyGroupAttendanceResponse;
import jedrekp.daycarecateringbillgenerator.entity.Child;
import jedrekp.daycarecateringbillgenerator.repository.AttendanceSheetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@SpringBootTest
class AttendanceServiceTest {

    @Mock
    private AttendanceSheetRepository attendanceSheetRepository;
    @Mock
    private ChildService childService;
    @InjectMocks
    private AttendanceService attendanceService;

    private Child testChild1;
    private Child testChild2;
    private Child testChild3;

    @BeforeEach
    void setUp() {
        buildChildObjectsForTesting();
    }

    private void buildChildObjectsForTesting() {
        testChild1 = new Child("Son", "Gohan", "songoku@fake.fake", false);
        ReflectionTestUtils.setField(testChild1, "id", 1);

        testChild2 = new Child("Luke", "Skywalker", "darthvader@fake.fake", false);
        ReflectionTestUtils.setField(testChild2, "id", 2);

        testChild3 = new Child("Norman", "Bates", "normabates@fake.fake", false);
        ReflectionTestUtils.setField(testChild3, "id", 3);
    }

    @Test
    void test_getDailyAttendanceForDaycareGroup() {
        LocalDate testedDate = LocalDate.of(2020,4,10);
        when(childService.findPresentChildrenByDateAndDaycareGroupId(testedDate,1L))
                .thenReturn(Arrays.asList(testChild1, testChild2));
        when(childService.findAbsentChildrenByDateAndDaycareGroupId(testedDate, 1))
                .thenReturn(Collections.singletonList(testChild3));

        DailyGroupAttendanceResponse attendanceResponse = attendanceService.getDailyAttendanceForDaycareGroup(
                testedDate,1L);

        assertAll(
                "Daily group attendance response object has all values as expected.",
                () -> assertEquals(1L, attendanceResponse.getDaycareGroupId()),
                () -> assertEquals(testedDate, attendanceResponse.getDate()),
                () -> assertEquals(Set.of(1L, 2L), attendanceResponse.getPresentChildrenIds()),
                () -> assertEquals(Collections.singleton(3L), attendanceResponse.getAbsentChildrenIds() )
        );

        verify(childService,times(1)).findPresentChildrenByDateAndDaycareGroupId(testedDate,1L);
        verify(childService,times(1)).findAbsentChildrenByDateAndDaycareGroupId(testedDate,1L);
        verifyNoMoreInteractions(childService,attendanceSheetRepository);
    }

    @Test
    void test_getDailyAttendanceForDaycareGroup_whenAttendanceNotYetTrackedForGivenDateAndGroup(){
        LocalDate testedDate = LocalDate.of(2020,8,15);
        when(childService.findPresentChildrenByDateAndDaycareGroupId(testedDate,2L))
                .thenReturn(Collections.emptyList());
        when(childService.findAbsentChildrenByDateAndDaycareGroupId(testedDate, 2L))
                .thenReturn(Collections.emptyList());

        DailyGroupAttendanceResponse attendanceResponse = attendanceService.getDailyAttendanceForDaycareGroup(
                testedDate,2L);

        assertAll(
                "Date and groupId fields in attendanceResponse same as method arguments, both presentChildrenIds and absentChildrenIds sets are empty",
                () -> assertEquals(2L, attendanceResponse.getDaycareGroupId()),
                () -> assertEquals(testedDate, attendanceResponse.getDate()),
                () -> assertEquals(Collections.emptySet(), attendanceResponse.getPresentChildrenIds()),
                () -> assertEquals(Collections.emptySet(), attendanceResponse.getAbsentChildrenIds())
        );

        verify(childService,times(1)).findPresentChildrenByDateAndDaycareGroupId(testedDate,2L);
        verify(childService,times(1)).findAbsentChildrenByDateAndDaycareGroupId(testedDate,2);
        verifyNoMoreInteractions(childService,attendanceSheetRepository);
    }

}
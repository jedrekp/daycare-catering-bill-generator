package jedrekp.daycarecateringbillgenerator.service;

import jedrekp.daycarecateringbillgenerator.DTO.response.ChildMonthlyAttendanceResponse;
import jedrekp.daycarecateringbillgenerator.DTO.response.DailyGroupAttendanceResponse;
import jedrekp.daycarecateringbillgenerator.entity.AttendanceSheet;
import jedrekp.daycarecateringbillgenerator.entity.Child;
import jedrekp.daycarecateringbillgenerator.repository.AttendanceSheetRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@SpringBootTest
@SuppressWarnings("SpellCheckingInspection")
class AttendanceServiceTest {

    @Mock
    private AttendanceSheetRepository attendanceSheetRepository;
    @Mock
    private ChildService childService;
    @InjectMocks
    private AttendanceService attendanceService;


    //getDailyAttendanceForDaycareGroup

    @Test
    void shouldReturnDailyGroupAttendanceObjectWithPresentChildrenIdsAndAbsentChildrenIdsSetsPopulatedWithCorrectIds() {
        //given
        Child testChild1 = new Child("Son", "Gohan", "songoku@fake.fake", false);
        ReflectionTestUtils.setField(testChild1, "id", 1L);
        Child testChild2 = new Child("Luke", "Skywalker", "darthvader@fake.fake", false);
        ReflectionTestUtils.setField(testChild2, "id", 2L);
        Child testChild3 = new Child("Norman", "Bates", "normabates@fake.fake", false);
        ReflectionTestUtils.setField(testChild3, "id", 3L);

        LocalDate testDate = LocalDate.of(2020, 4, 10);

        when(childService.findPresentChildrenByDateAndDaycareGroupId(testDate, 1L))
                .thenReturn(Arrays.asList(testChild1, testChild2));
        when(childService.findAbsentChildrenByDateAndDaycareGroupId(testDate, 1))
                .thenReturn(Collections.singletonList(testChild3));

        //when
        DailyGroupAttendanceResponse attendanceResponse = attendanceService.getDailyAttendanceForDaycareGroup(
                testDate, 1L);

        //then
        assertAll(
                "Date and groupId fields in attendanceResponse same as method parameters, both presentChildrenIds and absentChildrenIds sets populated with correct child ids.",
                () -> assertEquals(1L, attendanceResponse.getDaycareGroupId()),
                () -> assertEquals(testDate, attendanceResponse.getDate()),
                () -> assertEquals(Set.of(1L, 2L), attendanceResponse.getPresentChildrenIds()),
                () -> assertEquals(Collections.singleton(3L), attendanceResponse.getAbsentChildrenIds())
        );

        verify(childService, times(1)).findPresentChildrenByDateAndDaycareGroupId(testDate, 1L);
        verify(childService, times(1)).findAbsentChildrenByDateAndDaycareGroupId(testDate, 1L);
        verifyNoMoreInteractions(attendanceSheetRepository, childService);
    }

    @Test
    void shouldReturnDailyGroupAttendanceObjectWitEmptyPresentChildrenIdsAndAbsentChildrenIdsSets() {
        //given
        LocalDate testDate = LocalDate.of(2020, 8, 15);

        when(childService.findPresentChildrenByDateAndDaycareGroupId(testDate, 1L))
                .thenReturn(Collections.emptyList());
        when(childService.findAbsentChildrenByDateAndDaycareGroupId(testDate, 1L))
                .thenReturn(Collections.emptyList());

        //when
        DailyGroupAttendanceResponse attendanceResponse = attendanceService.getDailyAttendanceForDaycareGroup(
                testDate, 1L);

        //then
        assertAll(
                "Date and groupId fields in attendanceResponse same as method parameters, both presentChildrenIds and absentChildrenIds sets are empty",
                () -> assertEquals(1L, attendanceResponse.getDaycareGroupId()),
                () -> assertEquals(testDate, attendanceResponse.getDate()),
                () -> assertEquals(Collections.emptySet(), attendanceResponse.getPresentChildrenIds()),
                () -> assertEquals(Collections.emptySet(), attendanceResponse.getAbsentChildrenIds())
        );

        verify(childService, times(1)).findPresentChildrenByDateAndDaycareGroupId(testDate, 1L);
        verify(childService, times(1)).findAbsentChildrenByDateAndDaycareGroupId(testDate, 1L);
        verifyNoMoreInteractions(childService, attendanceSheetRepository);
    }



}
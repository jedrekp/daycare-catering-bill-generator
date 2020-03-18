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
                "Date and groupId fields in attendanceResponse same as method arguments, both presentChildrenIds and absentChildrenIds sets populated with correct child ids.",
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
                "Date and groupId fields in attendanceResponse same as method arguments, both presentChildrenIds and absentChildrenIds sets are empty",
                () -> assertEquals(1L, attendanceResponse.getDaycareGroupId()),
                () -> assertEquals(testDate, attendanceResponse.getDate()),
                () -> assertEquals(Collections.emptySet(), attendanceResponse.getPresentChildrenIds()),
                () -> assertEquals(Collections.emptySet(), attendanceResponse.getAbsentChildrenIds())
        );

        verify(childService, times(1)).findPresentChildrenByDateAndDaycareGroupId(testDate, 1L);
        verify(childService, times(1)).findAbsentChildrenByDateAndDaycareGroupId(testDate, 1L);
        verifyNoMoreInteractions(childService, attendanceSheetRepository);
    }


    //getMonthlyAttendanceForChild

    @Test
    void shouldReturnChildMonthlyAttendanceResponseObjectWithDatesWhenPresentAndDatesWhenAbsentSetsPopulatedWithCorrectDates() {
        //given
        Child testChild = new Child("Son", "Gohan", "songoku@fake.fake", false);
        ReflectionTestUtils.setField(testChild, "id", 1L);

        LocalDate testDate1 = LocalDate.of(2020, 4, 12);
        LocalDate testDate2 = LocalDate.of(2020, 4, 13);
        LocalDate testDate3 = LocalDate.of(2020, 4, 14);

        AttendanceSheet testAttendanceSheet1 = new AttendanceSheet(testDate1);
        testAttendanceSheet1.getPresentChildren().add(testChild);
        AttendanceSheet testAttendanceSheet2 = new AttendanceSheet(testDate2);
        testAttendanceSheet2.getPresentChildren().add(testChild);
        AttendanceSheet testAttendanceSheet3 = new AttendanceSheet(testDate3);
        testAttendanceSheet3.getAbsentChildren().add(testChild);

        when(attendanceSheetRepository.findByPresentChildIdForSpecificMonth(1L, 4, 2020))
                .thenReturn(Arrays.asList(testAttendanceSheet1, testAttendanceSheet2));
        when(attendanceSheetRepository.findByAbsentChildIdForSpecificMonth(1L, 4, 2020))
                .thenReturn(Collections.singletonList(testAttendanceSheet3));

        //when
        ChildMonthlyAttendanceResponse attendanceResponse = attendanceService.getMonthlyAttendanceForChild(1L, Month.APRIL, Year.of(2020));

        //then
        assertAll(
                "ChildId field same as method argument, datesWhenPresent and datesWhenAbsent sets populated with correct dates",
                () -> assertEquals(1L, attendanceResponse.getChildId()),
                () -> assertEquals(Set.of(testDate1, testDate2), attendanceResponse.getDatesWhenPresent()),
                () -> assertEquals(Collections.singleton(testDate3), attendanceResponse.getDatesWhenAbsent())
        );

        verify(attendanceSheetRepository, times(1)).findByPresentChildIdForSpecificMonth(1L, 4, 2020);
        verify(attendanceSheetRepository, times(1)).findByAbsentChildIdForSpecificMonth(1L, 4, 2020);
        verifyNoMoreInteractions(attendanceSheetRepository, childService);
    }

    @Test
    void shouldReturnChildMonthlyAttendanceResponseObjectWithEmptyDatesWhenPresentAndDatesWhenAbsentSetes() {
        //given
        when(attendanceSheetRepository.findByPresentChildIdForSpecificMonth(1L, 4, 2020))
                .thenReturn(Collections.emptyList());
        when(attendanceSheetRepository.findByAbsentChildIdForSpecificMonth(1L, 4, 2020))
                .thenReturn(Collections.emptyList());

        //when
        ChildMonthlyAttendanceResponse attendanceResponse = attendanceService.getMonthlyAttendanceForChild(1L, Month.APRIL, Year.of(2020));

        //then
        assertAll(
                "ChildId field same as method argument, datesWhenPresent and datesWhenAbsent sets both empty",
                () -> assertEquals(1L, attendanceResponse.getChildId()),
                () -> assertEquals(Collections.emptySet(), attendanceResponse.getDatesWhenPresent()),
                () -> assertEquals(Collections.emptySet(), attendanceResponse.getDatesWhenAbsent())
        );

        verify(attendanceSheetRepository, times(1)).findByPresentChildIdForSpecificMonth(1L, 4, 2020);
        verify(attendanceSheetRepository, times(1)).findByAbsentChildIdForSpecificMonth(1L, 4, 2020);
        verifyNoMoreInteractions(attendanceSheetRepository, childService);
    }

}
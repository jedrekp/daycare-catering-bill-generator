package jedrekp.daycarecateringbillgenerator.service;

import jedrekp.daycarecateringbillgenerator.DTO.request.TrackDailyGroupAttendanceRequest;
import jedrekp.daycarecateringbillgenerator.DTO.response.ChildMonthlyAttendanceResponse;
import jedrekp.daycarecateringbillgenerator.DTO.response.DailyGroupAttendanceResponse;
import jedrekp.daycarecateringbillgenerator.entity.AttendanceSheet;
import jedrekp.daycarecateringbillgenerator.entity.Child;
import jedrekp.daycarecateringbillgenerator.entity.DaycareGroup;
import jedrekp.daycarecateringbillgenerator.repository.AttendanceSheetRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
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
    void shouldReturnDailyGroupAttendanceResponseObjectWithChildIdSetsPopulatedWhenAttedanceTrackedForGivenDate() {
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
    void shouldReturnDailyGroupAttendanceResponseObjectWitEmptyChildIdSetsWhenAttendanceNotYetTrackedForGivenDate() {
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
                "Date and groupId fields in attendanceResponse same as method arguments," +
                        " both presentChildrenIds and absentChildrenIds sets are empty",
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
    void shouldReturnChildMonthlyAttendanceResponseObjectWithDateSetsPopulatedWhenAttendanceTrackedForGivenMonth() {
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
    void shouldReturnChildMonthlyAttendanceResponseObjectWithEmptyDateSetsWhenAttendanceNotYetTrackedForGivenMonth() {
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


    //submitDailyAttendanceForGroup

    @Test
    void shouldSaveAndReturnNewAttendanceSheetObjectWhenAttendanceNotYetTrackedForGivenDate() {
        //given
        Child testChild1 = new Child("Son", "Gohan", "songoku@fake.fake", false);
        ReflectionTestUtils.setField(testChild1, "id", 1L);
        Child testChild2 = new Child("Luke", "Skywalker", "darthvader@fake.fake", false);
        ReflectionTestUtils.setField(testChild2, "id", 4L);
        Child testChild3 = new Child("Norman", "Bates", "normabates@fake.fake", false);
        ReflectionTestUtils.setField(testChild3, "id", 3L);

        LocalDate testDate = LocalDate.of(2020, 3, 18); //Wednesday

        TrackDailyGroupAttendanceRequest attendanceRequest = new TrackDailyGroupAttendanceRequest(
                testDate,
                Set.of(1L, 2L), //toBeMarkedPresent
                Collections.singleton(3L) //toBeMarkedAbsent
        );

        when(attendanceSheetRepository.findByDateWithChildren(testDate)).thenReturn(Optional.empty());
        when(childService.findSingleChildByIdAndDaycareGroupId(1L, 1L)).thenReturn(testChild1);
        when(childService.findSingleChildByIdAndDaycareGroupId(2L, 1L)).thenReturn(testChild2);
        when(childService.findSingleChildByIdAndDaycareGroupId(3L, 1L)).thenReturn(testChild3);
        when(attendanceSheetRepository.save(any(AttendanceSheet.class))).thenAnswer(save -> save.getArguments()[0]);

        //when
        AttendanceSheet newAttendanceSheet = attendanceService.submitDailyAttendanceForGroup(attendanceRequest, 1L);

        //then
        assertAll(
                "Attendance sheet object has been created with correct date and correct children marked as present and absent",
                () -> assertEquals(testDate, newAttendanceSheet.getDate()),
                () -> assertEquals(Set.of(testChild1, testChild2), newAttendanceSheet.getPresentChildren()),
                () -> assertEquals(Collections.singleton(testChild3), newAttendanceSheet.getAbsentChildren())
        );

        verify(attendanceSheetRepository, times(1)).findByDateWithChildren(testDate);
        verify(childService, times(1)).findSingleChildByIdAndDaycareGroupId(1L, 1L);
        verify(childService, times(1)).findSingleChildByIdAndDaycareGroupId(2L, 1L);
        verify(childService, times(1)).findSingleChildByIdAndDaycareGroupId(3L, 1L);
        verify(attendanceSheetRepository, times(1)).save(newAttendanceSheet);
        verifyNoMoreInteractions(attendanceSheetRepository, childService);

    }

    @Test
    void shouldUpdateAndReturnExistingAttendanceObjectWhenSomeAttendancesAlreadyTrackedForGivenDate() {
        //given
        Child testChild1 = new Child("Son", "Gohan", "songoku@fake.fake", false);
        ReflectionTestUtils.setField(testChild1, "id", 1L);
        Child testChild2 = new Child("Luke", "Skywalker", "darthvader@fake.fake", false);
        ReflectionTestUtils.setField(testChild2, "id", 2L);
        Child testChild3 = new Child("Norman", "Bates", "normabates@fake.fake", false);
        ReflectionTestUtils.setField(testChild3, "id", 3L);
        Child testChild4 = new Child("John", "Snow", "eddstark@fake.fake", false);
        ReflectionTestUtils.setField(testChild4, "id", 4L);
        Child testChild5 = new Child("Stewie", "Griffin", "petergriffin@fake.fake", false);
        ReflectionTestUtils.setField(testChild5, "id", 5L);

        LocalDate testDate = LocalDate.of(2020, 3, 18); //Wednesday

        AttendanceSheet testAttendanceSheet = new AttendanceSheet(testDate);
        testAttendanceSheet.getPresentChildren().addAll(Set.of(testChild2, testChild5));
        testAttendanceSheet.getAbsentChildren().addAll(Set.of(testChild3, testChild4));

        TrackDailyGroupAttendanceRequest attendanceRequest = new TrackDailyGroupAttendanceRequest(
                testDate,
                Set.of(1L, 3L), //toBeMarkedPresent
                Set.of(2L, 4L) //toBeMarkedAbsent
        );

        when(attendanceSheetRepository.findByDateWithChildren(testDate)).thenReturn(Optional.of(testAttendanceSheet));
        when(childService.findSingleChildByIdAndDaycareGroupId(1L, 1L)).thenReturn(testChild1);
        when(childService.findSingleChildByIdAndDaycareGroupId(2L, 1L)).thenReturn(testChild2);
        when(childService.findSingleChildByIdAndDaycareGroupId(3L, 1L)).thenReturn(testChild3);
        when(childService.findSingleChildByIdAndDaycareGroupId(4L, 1L)).thenReturn(testChild4);
        when(attendanceSheetRepository.save(any(AttendanceSheet.class))).thenAnswer(save -> save.getArguments()[0]);

        //when
        AttendanceSheet updatedAttendanceSheet = attendanceService.submitDailyAttendanceForGroup(attendanceRequest, 1L);

        //then
        assertAll(("Expected attendance changes:" +
                        "\n Child#1 should be marked present (attendance not tracked before, marked present in update request)." +
                        "\n Child#2 should be changed to absent (previously present, but marked absent in update request)." +
                        "\n Child#3 should be changed to present (previously absent, but marked present in update request). " +
                        "\n Child#4 should still be marked absent (same as before, as also marked absent in update request)." +
                        "\n Child#5 should still be marked present (same as before, as childid not included in update request)."),
                () -> assertEquals(Set.of(testChild1, testChild3, testChild5), updatedAttendanceSheet.getPresentChildren()),
                () -> assertEquals(Set.of(testChild2, testChild4), updatedAttendanceSheet.getAbsentChildren())
        );

        verify(attendanceSheetRepository, times(1)).findByDateWithChildren(testDate);
        verify(childService, times(1)).findSingleChildByIdAndDaycareGroupId(1L, 1L);
        verify(childService, times(1)).findSingleChildByIdAndDaycareGroupId(2L, 1L);
        verify(childService, times(1)).findSingleChildByIdAndDaycareGroupId(3L, 1L);
        verify(childService, times(1)).findSingleChildByIdAndDaycareGroupId(4L, 1L);
        verify(attendanceSheetRepository, times(1)).save(updatedAttendanceSheet);
        verifyNoMoreInteractions(attendanceSheetRepository, childService);
    }

    @Test
    void shouldThrowExceptionWhenUpdateRequestHasSameChildIdInBothPrsentChildrenIdsAndAbsentChildrenIdsSets() {
        //given
        LocalDate testDate = LocalDate.of(2020, 3, 18); //Wednesday

        TrackDailyGroupAttendanceRequest attendanceRequest = new TrackDailyGroupAttendanceRequest(
                testDate,
                Set.of(1L, 2L), //toBeMarkedPresent
                Set.of(1L, 3L) //toBeMarkedAbsent
        );

        //expect
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class, () -> attendanceService.submitDailyAttendanceForGroup(attendanceRequest, 1L));
        assertEquals("One or more children are marked as both present and absent.", ex.getMessage());

        verifyZeroInteractions(attendanceSheetRepository, childService);
    }

    @Test
    void shouldThrowExceptionWhenDateInUpdateRequestIsNotWeekday() {
        //given
        LocalDate testDate = LocalDate.of(2020, 3, 21); //Saturday

        TrackDailyGroupAttendanceRequest attendanceRequest = new TrackDailyGroupAttendanceRequest(
                testDate,
                Set.of(1L, 2L), //toBeMarkedPresent
                Set.of(3L, 4L) //toBeMarkedAbsent
        );

        //expect
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class, () -> attendanceService.submitDailyAttendanceForGroup(attendanceRequest, 1L));
        assertEquals("2020-03-21 is not a weekday.", ex.getMessage());

        verifyZeroInteractions(attendanceSheetRepository, childService);
    }

    @Test
    void shouldThrowExceptionWhenAtLeastOneChildMarkedPresentInUpdateRequestNotFoundByIdAndDaycareGroupId() {
        //given
        LocalDate testDate = LocalDate.of(2020, 3, 18); //Wednesday

        TrackDailyGroupAttendanceRequest attendanceRequest = new TrackDailyGroupAttendanceRequest(
                testDate,
                Collections.singleton(1L), //toBeMarkedPresent
                Collections.emptySet() //toBeMarkedAbsent
        );

        when(attendanceSheetRepository.findByDateWithChildren(testDate)).thenReturn(Optional.empty());
        when(childService.findSingleChildByIdAndDaycareGroupId(1L, 1L))
                .thenThrow(new EntityNotFoundException("Child #1 does not exist or is not assigned to daycare group #1."));

        //expect
        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class, () -> attendanceService.submitDailyAttendanceForGroup(attendanceRequest, 1L));
        assertEquals("Child #1 does not exist or is not assigned to daycare group #1.", ex.getMessage());

        verify(attendanceSheetRepository, times(1)).findByDateWithChildren(testDate);
        verify(childService, times(1)).findSingleChildByIdAndDaycareGroupId(1L, 1L);
        verifyNoMoreInteractions(attendanceSheetRepository, childService);
    }

    @Test
    void shouldThrowExceptionWhenAtLeastOneChildMarkedAbsentInUpdateRequestNotFoundByIdAndDaycareGroupId() {
        //given
        LocalDate testDate = LocalDate.of(2020, 3, 18); //Wednesday

        TrackDailyGroupAttendanceRequest attendanceRequest = new TrackDailyGroupAttendanceRequest(
                testDate,
                Collections.emptySet(), //toBeMarkedPresent
                Collections.singleton(1L) //toBeMarkedAbsent
        );

        when(attendanceSheetRepository.findByDateWithChildren(testDate)).thenReturn(Optional.empty());
        when(childService.findSingleChildByIdAndDaycareGroupId(1L, 1L))
                .thenThrow(new EntityNotFoundException("Child #1 does not exist or is not assigned to daycare group #1."));

        //expect
        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class, () -> attendanceService.submitDailyAttendanceForGroup(attendanceRequest, 1L));
        assertEquals("Child #1 does not exist or is not assigned to daycare group #1.", ex.getMessage());

        verify(attendanceSheetRepository, times(1)).findByDateWithChildren(testDate);
        verify(childService, times(1)).findSingleChildByIdAndDaycareGroupId(1L, 1L);
        verifyNoMoreInteractions(attendanceSheetRepository, childService);
    }

}
package jedrekp.daycarecateringbillgenerator.service;

import jedrekp.daycarecateringbillgenerator.DTO.request.TrackDailyGroupAttendanceRequest;
import jedrekp.daycarecateringbillgenerator.DTO.request.UpdateMonthlyAttendanceForChildRequest;
import jedrekp.daycarecateringbillgenerator.DTO.response.ChildMonthlyAttendanceResponse;
import jedrekp.daycarecateringbillgenerator.DTO.response.DailyGroupAttendanceResponse;
import jedrekp.daycarecateringbillgenerator.entity.AttendanceSheet;
import jedrekp.daycarecateringbillgenerator.entity.Child;
import jedrekp.daycarecateringbillgenerator.repository.AttendanceSheetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.text.MessageFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceSheetRepository attendanceSheetRepository;

    private final ChildService childService;

    @Transactional(readOnly = true)
    public DailyGroupAttendanceResponse getDailyAttendanceForDaycareGroup(LocalDate date, long daycareGroupId) {

        DailyGroupAttendanceResponse dailyGroupAttendanceResponse = new DailyGroupAttendanceResponse(daycareGroupId, date);

        dailyGroupAttendanceResponse.setPresentChildrenIds(childService.findPresentChildrenByDateAndDaycareGroupId(date, daycareGroupId)
                .stream()
                .map(Child::getId)
                .collect(Collectors.toSet()));

        dailyGroupAttendanceResponse.setAbsentChildrenIds(childService.findAbsentChildrenByDateAndDaycareGroupId(date, daycareGroupId)
                .stream()
                .map(Child::getId)
                .collect(Collectors.toSet()));

        return dailyGroupAttendanceResponse;
    }

    @Transactional(readOnly = true)
    public ChildMonthlyAttendanceResponse getMonthlyAttendanceForChild(
            long childId, Month month, Year year) {

        if (!childService.existsById(childId)) {
            throw new EntityNotFoundException(MessageFormat.format("Child#{0} does not exist.", childId));
        }

        ChildMonthlyAttendanceResponse attendanceResponse = new ChildMonthlyAttendanceResponse(childId);

        attendanceResponse.setDatesWhenPresent(attendanceSheetRepository.findByPresentChildIdForSpecificMonth(
                childId, month.getValue(), year.getValue())
                .stream()
                .map(AttendanceSheet::getDate)
                .collect(Collectors.toSet()));

        attendanceResponse.setDatesWhenAbsent(attendanceSheetRepository.findByAbsentChildIdForSpecificMonth(
                childId, month.getValue(), year.getValue())
                .stream()
                .map(AttendanceSheet::getDate)
                .collect(Collectors.toSet()));

        return attendanceResponse;
    }

    @Transactional
    public AttendanceSheet submitDailyAttendanceForGroup(TrackDailyGroupAttendanceRequest attendanceRequest, long daycareGroupId) {

        verifyNoChildMarkedAsBothPresentAndAbsent(attendanceRequest);
        verifyIfDateIsWeekday(attendanceRequest.getDate());

        AttendanceSheet attendanceSheet = findByDateOrElseCreateNew(attendanceRequest.getDate());
        addPresentChildrenToAttendanceSheet(attendanceSheet, attendanceRequest.getIdsOfChildrenToMarkAsPresent(), daycareGroupId);
        addAbsentChildrenToAttendanceSheet(attendanceSheet, attendanceRequest.getIdsOfChildrenToMarkAsAbsent(), daycareGroupId);
        return attendanceSheetRepository.save(attendanceSheet);
    }

    @Transactional
    public List<AttendanceSheet> submitMonthlyAttendanceChangesForChild(
            long childId, Month month, Year year, UpdateMonthlyAttendanceForChildRequest attendanceRequest) {

        verifyChildNotMarkedBothPresentAndAbsentForSameDate(attendanceRequest);
        verifyIfAllDatesAreWithinTheSelectedMonth(attendanceRequest, month, year);
        verifyIfAllDatesAreWeekdays(attendanceRequest);

        Child child = childService.findSingleNotArchivedChildById(childId);
        List<AttendanceSheet> attendanceSheets = new ArrayList<>();
        markSingleChildAsPresentForGivenDates(attendanceRequest.getDatesToMarkAsPresent(), attendanceSheets, child);
        markSingleChildAsAbsentForGivenDates(attendanceRequest.getDatesToMarkAsAbsent(), attendanceSheets, child);

        attendanceSheets.sort(Comparator.comparing(AttendanceSheet::getDate));
        return attendanceSheets;
    }

    private AttendanceSheet findByDateOrElseCreateNew(LocalDate date) {
        return attendanceSheetRepository
                .findByDateWithChildren(date)
                .orElse(new AttendanceSheet(date));
    }

    private void addPresentChildrenToAttendanceSheet(AttendanceSheet attendanceSheet, Set<Long> presentChildrenIds, long daycareGroupId) {
        if (!attendanceSheet.getAbsentChildren().isEmpty()) {
            removeChildrenMarkedPresentFromAbsentChildrenSet(attendanceSheet, presentChildrenIds);
        }
        presentChildrenIds.forEach(childId -> attendanceSheet.getPresentChildren()
                .add(childService.findSingleChildByIdAndDaycareGroupId(childId, daycareGroupId)));
    }

    private void removeChildrenMarkedPresentFromAbsentChildrenSet(AttendanceSheet attendanceSheet, Set<Long> presentChildrenIds) {
        attendanceSheet.getAbsentChildren().removeAll(
                attendanceSheet.getAbsentChildren()
                        .stream()
                        .filter(child -> presentChildrenIds.contains(child.getId()))
                        .collect(Collectors.toSet())
        );
    }

    private void addAbsentChildrenToAttendanceSheet(AttendanceSheet attendanceSheet, Set<Long> absentChildrenIds, long daycareGroupId) {
        if (!attendanceSheet.getPresentChildren().isEmpty()) {
            removeChildrenMarkedAbsentFromPresentChildrenSet(attendanceSheet, absentChildrenIds);
        }
        absentChildrenIds.forEach(childId -> attendanceSheet.getAbsentChildren()
                .add(childService.findSingleChildByIdAndDaycareGroupId(childId, daycareGroupId)));
    }

    private void removeChildrenMarkedAbsentFromPresentChildrenSet(AttendanceSheet attendanceSheet, Set<Long> absentChildrenIds) {
        attendanceSheet.getPresentChildren().removeAll(
                attendanceSheet.getPresentChildren()
                        .stream()
                        .filter(child -> absentChildrenIds.contains(child.getId()))
                        .collect(Collectors.toSet())
        );
    }

    private void markSingleChildAsPresentForGivenDates(
            Set<LocalDate> datesToMarkAsPresent, List<AttendanceSheet> attendanceSheets, Child child) {
        for (LocalDate date : datesToMarkAsPresent) {
            attendanceSheetRepository.findByDateWithChildren(date).ifPresentOrElse(attendanceSheet ->
                    {
                        attendanceSheet.getPresentChildren().add(child);
                        attendanceSheet.getAbsentChildren().remove(child);
                        attendanceSheets.add(attendanceSheet);
                    },
                    () -> {
                        AttendanceSheet attendanceSheet = new AttendanceSheet(date);
                        attendanceSheet.getPresentChildren().add(child);
                        attendanceSheets.add(attendanceSheetRepository.save(attendanceSheet));
                    });
        }
    }

    private void markSingleChildAsAbsentForGivenDates(
            Set<LocalDate> datesToMarkAsAbsent, List<AttendanceSheet> attendanceSheets, Child child) {
        for (LocalDate date : datesToMarkAsAbsent) {
            attendanceSheetRepository.findByDateWithChildren(date).ifPresentOrElse(attendanceSheet ->
                    {
                        attendanceSheet.getAbsentChildren().add(child);
                        attendanceSheet.getPresentChildren().remove(child);
                        attendanceSheets.add(attendanceSheet);
                    },
                    () -> {
                        AttendanceSheet attendanceSheet = new AttendanceSheet(date);
                        attendanceSheet.getAbsentChildren().add(child);
                        attendanceSheets.add(attendanceSheetRepository.save(attendanceSheet));
                    });
        }
    }

    private void verifyNoChildMarkedAsBothPresentAndAbsent(TrackDailyGroupAttendanceRequest attendanceRequest) {
        if (!Collections.disjoint(attendanceRequest.getIdsOfChildrenToMarkAsPresent(), attendanceRequest.getIdsOfChildrenToMarkAsAbsent())) {
            throw new IllegalArgumentException("One or more children are marked as both present and absent.");
        }
    }

    private void verifyChildNotMarkedBothPresentAndAbsentForSameDate(UpdateMonthlyAttendanceForChildRequest attendanceRequest) {
        if (!Collections.disjoint(attendanceRequest.getDatesToMarkAsPresent(), attendanceRequest.getDatesToMarkAsAbsent())) {
            throw new IllegalArgumentException("Attendance update consists of dates for which child is marked as both present and absent");
        }
    }

    private void verifyIfAllDatesAreWithinTheSelectedMonth(UpdateMonthlyAttendanceForChildRequest request, Month month, Year year) {
        if (Stream.of(request.getDatesToMarkAsPresent(), request.getDatesToMarkAsAbsent())
                .flatMap(Collection::stream)
                .anyMatch(date -> date.getMonth() != month || date.getYear() != year.getValue())) {
            throw new IllegalArgumentException("Some of the dates are not within the selected month.");
        }
    }

    private void verifyIfDateIsWeekday(LocalDate date) {
        if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new IllegalArgumentException(MessageFormat.format("{0} is not a weekday.", date.toString()));
        }
    }

    private void verifyIfAllDatesAreWeekdays(UpdateMonthlyAttendanceForChildRequest attendanceRequest) {
        attendanceRequest.getDatesToMarkAsPresent().forEach(this::verifyIfDateIsWeekday);
        attendanceRequest.getDatesToMarkAsAbsent().forEach(this::verifyIfDateIsWeekday);
    }
}

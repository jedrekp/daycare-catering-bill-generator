package jedrekp.daycarecateringbillgenerator.service;

import jedrekp.daycarecateringbillgenerator.DTO.DailyGroupAttendanceDTO;
import jedrekp.daycarecateringbillgenerator.DTO.SingleChildMonthlyAttendanceDTO;
import jedrekp.daycarecateringbillgenerator.entity.AttendanceSheet;
import jedrekp.daycarecateringbillgenerator.entity.Child;
import jedrekp.daycarecateringbillgenerator.repository.AttendanceSheetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public AttendanceSheet submitDailyAttendanceForGroup(DailyGroupAttendanceDTO dailyGroupAttendanceDTO) {

        if (!Collections.disjoint(dailyGroupAttendanceDTO.getPresentChildrenIds(), dailyGroupAttendanceDTO.getAbsentChildrenIds())) {
            throw new IllegalArgumentException("One or more children are marked as both present and absent.");
        }

        AttendanceSheet attendanceSheet = findByDateOrElseCreateNew(dailyGroupAttendanceDTO.getDate());
        addPresentChildrenToAttendanceSheet(attendanceSheet, dailyGroupAttendanceDTO.getPresentChildrenIds());
        addAbsentChildrenToAttendanceSheet(attendanceSheet, dailyGroupAttendanceDTO.getAbsentChildrenIds());
        return attendanceSheetRepository.save(attendanceSheet);
    }

    @Transactional(readOnly = true)
    public DailyGroupAttendanceDTO getDailyAttendanceForDaycareGroup(long daycareGroupId, LocalDate date) {

        DailyGroupAttendanceDTO dailyGroupAttendanceDTO = new DailyGroupAttendanceDTO(daycareGroupId, date);

        dailyGroupAttendanceDTO.setPresentChildrenIds(childService.findPresentChildrenByDateAndDaycareGroupId(date, daycareGroupId)
                .stream()
                .map(Child::getId)
                .collect(Collectors.toSet()));

        dailyGroupAttendanceDTO.setAbsentChildrenIds(childService.findAbsentChildrenByDateAndDaycareGroupId(date, daycareGroupId)
                .stream()
                .map(Child::getId)
                .collect(Collectors.toSet()));

        return dailyGroupAttendanceDTO;
    }

    @Transactional
    public List<AttendanceSheet> submitMonthlyAttendanceChangesForChild(
            long childId, Month month, Year year, SingleChildMonthlyAttendanceDTO monthlyAttendanceDTO) {

        if (!Collections.disjoint(monthlyAttendanceDTO.getDaysWhenPresent(), monthlyAttendanceDTO.getDaysWhenAbsent())) {
            throw new IllegalArgumentException("Attendance update consists of dates for which child is marked as both present and absent");
        }
        verifyIfAllDatesAreWithinTheSelectedMonth(monthlyAttendanceDTO, month, year);

        Child child = childService.findSingleNotArchivedChildById(childId);
        List<AttendanceSheet> attendanceSheets = new ArrayList<>();
        markSingleChildAsPresentForGivenDates(monthlyAttendanceDTO.getDaysWhenPresent(), attendanceSheets, child);
        markSingleChildAsAbsentForGivenDates(monthlyAttendanceDTO.getDaysWhenAbsent(), attendanceSheets, child);

        attendanceSheets.sort(Comparator.comparing(AttendanceSheet::getDate));
        return attendanceSheets;
    }

    @Transactional(readOnly = true)
    public SingleChildMonthlyAttendanceDTO getMonthlyAttendanceForChild(long childId, Month month, Year year) {

        SingleChildMonthlyAttendanceDTO attendanceDTO = new SingleChildMonthlyAttendanceDTO(childId);

        attendanceDTO.setDaysWhenPresent(attendanceSheetRepository.findByPresentChildIdForSpecificMonth(childId, month.getValue(), year.getValue())
                .stream()
                .map(AttendanceSheet::getDate)
                .collect(Collectors.toSet()));

        attendanceDTO.setDaysWhenAbsent(attendanceSheetRepository.findByAbsentChildIdForSpecificMonth(childId, month.getValue(), year.getValue())
                .stream()
                .map(AttendanceSheet::getDate)
                .collect(Collectors.toSet()));

        return attendanceDTO;
    }

    private AttendanceSheet findByDateOrElseCreateNew(LocalDate date) {
        return attendanceSheetRepository
                .findByDateWithChildren(date)
                .orElse(new AttendanceSheet(date));
    }

    private void addPresentChildrenToAttendanceSheet(AttendanceSheet attendanceSheet, Set<Long> presentChildrenIds) {
        if (!attendanceSheet.getAbsentChildren().isEmpty())
            attendanceSheet.getAbsentChildren().removeAll(
                    attendanceSheet.getAbsentChildren()
                            .stream()
                            .filter(child -> presentChildrenIds.contains(child.getId()))
                            .collect(Collectors.toSet())
            );

        presentChildrenIds.forEach(childId -> attendanceSheet.getPresentChildren()
                .add(childService.findSingleNotArchivedChildById(childId)));
    }

    private void addAbsentChildrenToAttendanceSheet(AttendanceSheet attendanceSheet, Set<Long> absentChildrenIds) {
        if (!attendanceSheet.getPresentChildren().isEmpty())
            attendanceSheet.getPresentChildren().removeAll(
                    attendanceSheet.getPresentChildren()
                            .stream()
                            .filter(child -> absentChildrenIds.contains(child.getId()))
                            .collect(Collectors.toSet())
            );

        absentChildrenIds.forEach(childId -> attendanceSheet.getAbsentChildren()
                .add(childService.findSingleNotArchivedChildById(childId)));
    }

    private void markSingleChildAsPresentForGivenDates(
            Set<LocalDate> daysWhenPresent, List<AttendanceSheet> attendanceSheets, Child child) {
        for (LocalDate date : daysWhenPresent) {
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
            Set<LocalDate> daysWhenAbsent, List<AttendanceSheet> attendanceSheets, Child child) {
        for (LocalDate date : daysWhenAbsent) {
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

    private void verifyIfAllDatesAreWithinTheSelectedMonth(SingleChildMonthlyAttendanceDTO attendanceDTO, Month month, Year year) {
        if (Stream.of(attendanceDTO.getDaysWhenPresent(), attendanceDTO.getDaysWhenAbsent())
                .flatMap(Collection::stream)
                .anyMatch(date -> date.getMonth() != month || date.getYear() != year.getValue())) {
            throw new IllegalArgumentException("Some of the dates are not within the selected month.");
        }

    }

}

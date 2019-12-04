package jedrekp.daycarecateringbillgenerator.service;

import jedrekp.daycarecateringbillgenerator.DTO.DailyGroupAttendanceDTO;
import jedrekp.daycarecateringbillgenerator.DTO.SingleChildMonthlyAttendanceDTO;
import jedrekp.daycarecateringbillgenerator.entity.Child;
import jedrekp.daycarecateringbillgenerator.entity.DailyAttendance;
import jedrekp.daycarecateringbillgenerator.entity.DaycareGroup;
import jedrekp.daycarecateringbillgenerator.repository.DailyAttendanceRepository;
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
public class DailyAttendanceService {

    private final DailyAttendanceRepository dailyAttendanceRepository;

    private final ChildService childService;

    private final DaycareGroupService daycareGroupService;

    @Transactional
    public DailyAttendance submitAttendanceForGroup(DailyGroupAttendanceDTO dailyGroupAttendanceDTO) {

        if (!Collections.disjoint(dailyGroupAttendanceDTO.getPresentChildrenIds(), dailyGroupAttendanceDTO.getAbsentChildrenIds())) {
            throw new IllegalArgumentException("One or more children are marked as both present and absent.");
        }

        DailyAttendance dailyAttendance = findByDateOrElseCreateNew(dailyGroupAttendanceDTO.getDate());
        addPresentChildrenToDailyAttendance(dailyAttendance, dailyGroupAttendanceDTO.getPresentChildrenIds());
        addAbsentChildrenToDailyAttendance(dailyAttendance, dailyGroupAttendanceDTO.getAbsentChildrenIds());
        return dailyAttendanceRepository.save(dailyAttendance);
    }

    @Transactional(readOnly = true)
    public DailyGroupAttendanceDTO getDailyAttendanceForDaycareGroup(long daycareGroupId, LocalDate date) {

        DaycareGroup daycareGroup = daycareGroupService.findSingleGroupByIdWithChildren(daycareGroupId);
        DailyGroupAttendanceDTO dailyGroupAttendanceDTO = new DailyGroupAttendanceDTO(date);

        Optional<DailyAttendance> optionalDailyAttendance = dailyAttendanceRepository
                .findByDateAndDaycareGroupIdWithPresentChildren(date, daycareGroupId);
        optionalDailyAttendance.ifPresent(o -> o.getPresentChildren()
                .forEach(child -> dailyGroupAttendanceDTO.getPresentChildrenIds().add(child.getId())));

        daycareGroup.getChildren()
                .stream().map(Child::getId)
                .filter(id -> !dailyGroupAttendanceDTO.getPresentChildrenIds().contains(id))
                .forEach(dailyGroupAttendanceDTO.getAbsentChildrenIds()::add);

        return dailyGroupAttendanceDTO;
    }

    @Transactional
    public List<DailyAttendance> submitMonthlyAttendanceChangesForChild(
            long childId, Month month, Year year, SingleChildMonthlyAttendanceDTO monthlyAttendanceDTO) {

        if (!Collections.disjoint(monthlyAttendanceDTO.getDaysWhenPresent(), monthlyAttendanceDTO.getDaysWhenAbsent())) {
            throw new IllegalArgumentException("Attendance update consists of dates for which child is marked as both present and absent");
        }

        verifyIfAllDatesAreInTheSameMonth(monthlyAttendanceDTO, month, year);

        Child child = childService.findSingleNotArchivedChildById(childId);

        List<DailyAttendance> dailyAttendances = new ArrayList<>();

        for (LocalDate date : monthlyAttendanceDTO.getDaysWhenPresent()) {
            dailyAttendanceRepository.findByDateWithChildren(date).ifPresentOrElse(dailyAttendance ->
                    {
                        dailyAttendance.getPresentChildren().add(child);
                        dailyAttendance.getAbsentChildren().remove(child);
                        dailyAttendances.add(dailyAttendance);
                    },
                    () -> {
                        DailyAttendance dailyAttendance = new DailyAttendance(date);
                        dailyAttendance.getPresentChildren().add(child);
                        dailyAttendances.add(dailyAttendanceRepository.save(dailyAttendance));
                    });
        }

        for (LocalDate date : monthlyAttendanceDTO.getDaysWhenAbsent()) {
            dailyAttendanceRepository.findByDateWithChildren(date).ifPresentOrElse(dailyAttendance ->
                    {
                        dailyAttendance.getAbsentChildren().add(child);
                        dailyAttendance.getPresentChildren().remove(child);
                        dailyAttendances.add(dailyAttendance);
                    },
                    () -> {
                        DailyAttendance dailyAttendance = new DailyAttendance(date);
                        dailyAttendance.getAbsentChildren().add(child);
                        dailyAttendances.add(dailyAttendanceRepository.save(dailyAttendance));
                    });
        }

        dailyAttendances.sort(Comparator.comparing(DailyAttendance::getDate));
        return dailyAttendances;
    }

    @Transactional(readOnly = true)
    public SingleChildMonthlyAttendanceDTO getMonthlyAttendanceForChild(long childId, Month month, Year year) {

        SingleChildMonthlyAttendanceDTO attendanceDTO = new SingleChildMonthlyAttendanceDTO();

        dailyAttendanceRepository.findByPresentChildIdForSpecificMonth(childId, month.getValue(), year.getValue())
                .stream()
                .map(DailyAttendance::getDate)
                .forEach(attendanceDTO.getDaysWhenPresent()::add);

        dailyAttendanceRepository.findByAbsentChildIdForSpecificMonth(childId, month.getValue(), year.getValue())
                .stream()
                .map(DailyAttendance::getDate)
                .forEach(attendanceDTO.getDaysWhenAbsent()::add);

        return attendanceDTO;
    }

    private DailyAttendance findByDateOrElseCreateNew(LocalDate date) {
        return dailyAttendanceRepository
                .findByDateWithChildren(date)
                .orElse(new DailyAttendance(date));
    }

    private void addPresentChildrenToDailyAttendance(DailyAttendance dailyAttendance, Set<Long> presentChildrenIds) {
        if (!dailyAttendance.getAbsentChildren().isEmpty())
            dailyAttendance.getAbsentChildren().removeAll(
                    dailyAttendance.getAbsentChildren()
                            .stream()
                            .filter(child -> presentChildrenIds.contains(child.getId()))
                            .collect(Collectors.toSet())
            );

        presentChildrenIds.forEach(childId -> dailyAttendance.getPresentChildren()
                .add(childService.findSingleNotArchivedChildById(childId)));
    }

    private void addAbsentChildrenToDailyAttendance(DailyAttendance dailyAttendance, Set<Long> absentChildrenIds) {
        if (!dailyAttendance.getPresentChildren().isEmpty())
            dailyAttendance.getPresentChildren().removeAll(
                    dailyAttendance.getPresentChildren()
                            .stream()
                            .filter(child -> absentChildrenIds.contains(child.getId()))
                            .collect(Collectors.toSet())
            );

        absentChildrenIds.forEach(childId -> dailyAttendance.getAbsentChildren()
                .add(childService.findSingleNotArchivedChildById(childId)));
    }

    private void verifyIfAllDatesAreInTheSameMonth(SingleChildMonthlyAttendanceDTO attendanceDTO, Month month, Year year) {
        if (Stream.of(attendanceDTO.getDaysWhenPresent(), attendanceDTO.getDaysWhenAbsent())
                .flatMap(Collection::stream)
                .anyMatch(date -> date.getMonth() != month || date.getYear() != year.getValue())) {
            throw new IllegalArgumentException("At least one date is not from the selected month");
        }

    }

}

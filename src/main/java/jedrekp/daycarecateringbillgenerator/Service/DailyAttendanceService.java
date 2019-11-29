package jedrekp.daycarecateringbillgenerator.Service;

import jedrekp.daycarecateringbillgenerator.DTO.DailyGroupAttendanceDTO;
import jedrekp.daycarecateringbillgenerator.DTO.SingleChildMonthlyAttendanceDTO;
import jedrekp.daycarecateringbillgenerator.Entity.Child;
import jedrekp.daycarecateringbillgenerator.Entity.DailyAttendance;
import jedrekp.daycarecateringbillgenerator.Entity.DaycareGroup;
import jedrekp.daycarecateringbillgenerator.Repository.DailyAttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DailyAttendanceService {

    @Autowired
    DailyAttendanceRepository dailyAttendanceRepository;

    @Autowired
    ChildService childService;

    @Autowired
    DaycareGroupService daycareGroupService;

    @Transactional
    public DailyAttendance submitAttendanceForGroup(DailyGroupAttendanceDTO dailyGroupAttendanceDTO) {

        if (!Collections.disjoint(dailyGroupAttendanceDTO.getPresentChildrenIds(), dailyGroupAttendanceDTO.getAbsentChildrenIds())) {
            throw new IllegalArgumentException("One or more children have been marked as both present or absent");
        }

        // find dailyAttendance object by date if already exists or else create new
        DailyAttendance dailyAttendance = dailyAttendanceRepository
                .findByDateWithChildren(dailyGroupAttendanceDTO.getDate())
                .orElse(new DailyAttendance(dailyGroupAttendanceDTO.getDate()));


        //remove children marked as absent from present children set
        dailyAttendance.getPresentChildren().removeAll(
                dailyAttendance.getPresentChildren()
                        .stream()
                        .filter(child -> dailyGroupAttendanceDTO.getAbsentChildrenIds().contains(child.getId()))
                        .collect(Collectors.toSet())
        );

        //remove children marked as present from absent children set
        dailyAttendance.getAbsentChildren().removeAll(
                dailyAttendance.getAbsentChildren()
                        .stream()
                        .filter(child -> dailyGroupAttendanceDTO.getPresentChildrenIds().contains(child.getId()))
                        .collect(Collectors.toSet())
        );

        //add children marked as present to present children set
        dailyGroupAttendanceDTO.getPresentChildrenIds()
                .forEach(childId -> dailyAttendance.getPresentChildren()
                        .add(childService.findSingleChildByIdAndArchived(childId, false)));

        //add children marked as absent to absent children set
        dailyGroupAttendanceDTO.getAbsentChildrenIds()
                .forEach(childId -> dailyAttendance.getAbsentChildren()
                        .add(childService.findSingleChildByIdAndArchived(childId, false)));

        return dailyAttendanceRepository.save(dailyAttendance);
    }

    @Transactional(readOnly = true)
    public DailyGroupAttendanceDTO getDailyAttendanceForDaycareGroup(Long daycareGroupId, LocalDate date) {

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
    public List<DailyAttendance> submitMonthlyAttendanceForChild(
            Long childId, SingleChildMonthlyAttendanceDTO monthlyAttendanceDTO) {

        Child child = childService.findSingleChildByIdAndArchived(childId, false);

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
    public SingleChildMonthlyAttendanceDTO getMonthlyAttendanceForChild(Long childId, Month month, Year year) {

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
}

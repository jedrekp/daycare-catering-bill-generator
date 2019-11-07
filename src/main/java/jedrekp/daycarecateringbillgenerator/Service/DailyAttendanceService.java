package jedrekp.daycarecateringbillgenerator.Service;

import jedrekp.daycarecateringbillgenerator.DTO.DailyAttendanceDTO;
import jedrekp.daycarecateringbillgenerator.Entity.Child;
import jedrekp.daycarecateringbillgenerator.Entity.DailyAttendance;
import jedrekp.daycarecateringbillgenerator.Entity.DaycareGroup;
import jedrekp.daycarecateringbillgenerator.Repository.DailyAttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
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
    public DailyAttendance markAttendance(DailyAttendanceDTO dailyAttendanceDTO) {

        if (!Collections.disjoint(dailyAttendanceDTO.getPresentChildrenIds(), dailyAttendanceDTO.getAbsentChildrenIds())) {
            throw new IllegalArgumentException("One or more children have been marked as both present or absent");
        }

        // find dailyAttendance object by date if already exists or else create new
        DailyAttendance dailyAttendance = dailyAttendanceRepository
                .findByDateWithPresentChildren(dailyAttendanceDTO.getDate())
                .orElse(new DailyAttendance(dailyAttendanceDTO.getDate()));


        //remove children marked as absent from present children set
        dailyAttendance.getPresentChildren().removeAll(
                dailyAttendance.getPresentChildren()
                        .stream()
                        .filter(child -> dailyAttendanceDTO.getAbsentChildrenIds().contains(child.getId()))
                        .collect(Collectors.toSet())
        );

        //remove children marked as present from absent children set
        dailyAttendance.getAbsentChildren().removeAll(
                dailyAttendance.getAbsentChildren()
                        .stream()
                        .filter(child -> dailyAttendanceDTO.getPresentChildrenIds().contains(child.getId()))
                        .collect(Collectors.toSet())
        );

        //add children marked as present to present children set
        dailyAttendanceDTO.getPresentChildrenIds()
                .forEach(childId -> dailyAttendance.getPresentChildren()
                        .add(childService.findSingleChildByIdAndArchived(childId, false)));

        //add children marked as absent to absent children set
        dailyAttendanceDTO.getAbsentChildrenIds()
                .forEach(childId -> dailyAttendance.getAbsentChildren()
                        .add(childService.findSingleChildByIdAndArchived(childId, false)));

        return dailyAttendanceRepository.save(dailyAttendance);
    }

    @Transactional(readOnly = true)
    public DailyAttendanceDTO getDailyAttendanceForDaycareGroup(Long daycareGroupId, LocalDate date) {
        DaycareGroup daycareGroup = daycareGroupService.findSingleGroupByIdWithChildren(daycareGroupId);
        DailyAttendanceDTO dailyAttendanceDTO = new DailyAttendanceDTO(date);

        Optional<DailyAttendance> optionalDailyAttendance = dailyAttendanceRepository
                .findByDateAndDaycareGroupIdWithPresentChildren(date, daycareGroupId);
        optionalDailyAttendance.ifPresent(o -> o.getPresentChildren()
                .forEach(child -> dailyAttendanceDTO.getPresentChildrenIds().add(child.getId())));

        daycareGroup.getChildren()
                .stream().map(Child::getId)
                .filter(id -> !dailyAttendanceDTO.getPresentChildrenIds().contains(id))
                .forEach(dailyAttendanceDTO.getAbsentChildrenIds()::add);

        return dailyAttendanceDTO;
    }
}

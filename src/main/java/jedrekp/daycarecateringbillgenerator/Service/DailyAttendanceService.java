package jedrekp.daycarecateringbillgenerator.Service;

import jedrekp.daycarecateringbillgenerator.DTO.DailyAttendanceDTO;
import jedrekp.daycarecateringbillgenerator.Entity.Child;
import jedrekp.daycarecateringbillgenerator.Entity.DailyAttendance;
import jedrekp.daycarecateringbillgenerator.Repository.DailyAttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DailyAttendanceService {

    @Autowired
    DailyAttendanceRepository dailyAttendanceRepository;

    @Autowired
    ChildService childService;

    @Transactional
    public DailyAttendance markAttendance(DailyAttendanceDTO dailyAttendanceDTO) {

        if (!Collections.disjoint(dailyAttendanceDTO.getPresentChildrenIds(), dailyAttendanceDTO.getAbsentChildrenIds())) {
            throw new IllegalArgumentException("One or more children have been marked as both present or absent");
        }

        // find dailyAttendance object by date if already exists or else create new
        DailyAttendance dailyAttendance = dailyAttendanceRepository
                .findByDateWithPresentChildren(dailyAttendanceDTO.getDate())
                .orElse(new DailyAttendance(dailyAttendanceDTO.getDate()));


        //remove absent children from dailyAttendance if they were previously marked as present
        Set<Child> childrenToRemove = dailyAttendance.getPresentChildren()
                .stream()
                .filter(child -> dailyAttendanceDTO.getAbsentChildrenIds().contains(child.getId()))
                .collect(Collectors.toSet());
        dailyAttendance.getPresentChildren().removeAll(childrenToRemove);

        //add children marked as present to dailyAttendance
        Set<Child> childrenToAdd = dailyAttendanceDTO.getPresentChildrenIds()
                .stream()
                .map(childId -> (childService.findSingleChildByIdAndArchived(childId, false)))
                .collect(Collectors.toSet());
        dailyAttendance.getPresentChildren().addAll(childrenToAdd);

        return dailyAttendanceRepository.save(dailyAttendance);
    }

    @Transactional(readOnly = true)
    public DailyAttendanceDTO getDailyAttendanceForDaycareGroup(Long daycareGroupId, LocalDate date) {
        DailyAttendanceDTO dailyAttendanceDTO = new DailyAttendanceDTO(date);
        Optional<DailyAttendance> optionalDailyAttendance = dailyAttendanceRepository
                .findByDateAndDaycareGroupIdWithPresentChildren(date, daycareGroupId);
        optionalDailyAttendance.ifPresent(o -> o.getPresentChildren()
                .forEach(child -> dailyAttendanceDTO.getPresentChildrenIds().add(child.getId())));
        return dailyAttendanceDTO;
    }
}

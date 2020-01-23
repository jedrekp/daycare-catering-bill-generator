package jedrekp.daycarecateringbillgenerator.utility;

import jedrekp.daycarecateringbillgenerator.repository.ChildRepository;
import jedrekp.daycarecateringbillgenerator.repository.DaycareEmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AttendanceTrackingPermissionEvaluator {

    private final DaycareEmployeeRepository daycareEmployeeRepository;
    private final ChildRepository childRepository;

    public boolean canMarkAttendanceForDaycareGroup(long daycareGroupId, Authentication authentication) {
        return daycareEmployeeRepository.existsByAppUsernameAndDaycareGroup_Id(authentication.getName(), daycareGroupId);
    }

    public boolean canMarkAttendanceForChild(long childId, Authentication authentication) {
        return childRepository.existsByIdAndDaycareGroup_GroupSupervisor_AppUsername(childId, authentication.getName());
    }

}

package jedrekp.daycarecateringbillgenerator.utility;

import jedrekp.daycarecateringbillgenerator.service.ChildService;
import jedrekp.daycarecateringbillgenerator.service.DaycareGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AttendanceTrackingPermissionEvaluator {

    private final DaycareGroupService daycareGroupService;
    private final ChildService childService;

    public boolean canMarkAttendanceForDaycareGroup(long daycareGroupId, Authentication authentication) {
        return daycareGroupService.verifyIfDaycareGroupIsSupervisedByUser(daycareGroupId, authentication.getName());
    }

    public boolean canMarkAttendanceForChild(long childId, Authentication authentication) {
        return childService.verifyIfChildIsAssignedToDaycareGroupSupervisedByUser(childId, authentication.getName());
    }

}

package jedrekp.daycarecateringbillgenerator.utility;

import lombok.Getter;

@Getter
public enum DaycareRole {
    HEADMASTER("headmaster"),
    GROUP_SUPERVISOR("group supervisor");

    private String roleName;

    DaycareRole(String roleName) {
        this.roleName = roleName;
    }
}

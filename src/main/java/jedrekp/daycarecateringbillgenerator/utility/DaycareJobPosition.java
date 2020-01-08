package jedrekp.daycarecateringbillgenerator.utility;

import lombok.Getter;

@Getter
public enum DaycareJobPosition {
    HEADMASTER("headmaster"),
    GROUP_SUPERVISOR("group supervisor");

    private String positionName;

    DaycareJobPosition(String positionName) {
        this.positionName = positionName;
    }
}

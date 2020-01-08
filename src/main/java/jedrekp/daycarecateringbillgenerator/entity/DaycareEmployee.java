package jedrekp.daycarecateringbillgenerator.entity;

import jedrekp.daycarecateringbillgenerator.utility.DaycareJobPosition;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "daycare_employee")
public class DaycareEmployee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    @Column(name = "first_name")
    private String firstName;
    @NotNull
    @Column(name = "last_name")
    private String lastName;
    @NotNull
    @Column(name = "app_username")
    private String appUsername;
    @NotNull
    private String password;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "daycare_job_position")
    private DaycareJobPosition daycareJobPosition;

}

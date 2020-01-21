package jedrekp.daycarecateringbillgenerator.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jedrekp.daycarecateringbillgenerator.utility.DaycareRole;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

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


    @Column(name = "first_name")
    @NotNull
    @Length(max = 20)
    private String firstName;

    @Column(name = "last_name")
    @NotNull
    @Length(max = 20)
    private String lastName;

    @Column(name = "app_username")
    @NotNull
    @Length(min = 5, max = 20)
    private String appUsername;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "daycare_role")
    @NotNull
    private DaycareRole daycareRole;

}

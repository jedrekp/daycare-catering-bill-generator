package jedrekp.daycarecateringbillgenerator.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import jedrekp.daycarecateringbillgenerator.utility.DaycareRole;
import jedrekp.daycarecateringbillgenerator.utility.JsonViewFilter;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "app_user")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @JsonView(JsonViewFilter.BasicInfo.class)
    private long id;

    @Column(name = "first_name")
    @JsonView(JsonViewFilter.BasicInfo.class)
    @NotNull
    @Length(min = 2, max = 20)
    private String firstName;

    @Column(name = "last_name")
    @JsonView(JsonViewFilter.BasicInfo.class)
    @NotNull
    @Length(min = 2, max = 20)
    private String lastName;

    @Column(name = "username", unique = true)
    @JsonView(JsonViewFilter.BasicInfo.class)
    @NotNull
    @Length(min = 5, max = 20)
    private String username;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "daycare_role")
    @JsonView(JsonViewFilter.BasicInfo.class)
    @NotNull
    private DaycareRole daycareRole;

    @OneToOne(mappedBy = "groupSupervisor")
    @JsonView(JsonViewFilter.BasicInfo.class)
    @JsonIgnoreProperties({"children", "groupSupervisor"})
    private DaycareGroup daycareGroup;


    public AppUser(
            @NotNull @Length(min = 2, max = 20) String firstName,
            @NotNull @Length(min = 2, max = 20) String lastName,
            @NotNull @Length(min = 5, max = 20) String username,
            @NotNull String password,
            @NotNull DaycareRole daycareRole
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.daycareRole = daycareRole;
    }
}

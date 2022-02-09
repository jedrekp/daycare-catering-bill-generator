package jedrekp.daycarecateringbillgenerator.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import jedrekp.daycarecateringbillgenerator.utility.JsonViewFilter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "daycare_group")
@Getter
@Setter
@NoArgsConstructor
public class DaycareGroup {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(JsonViewFilter.BasicInfo.class)
    private long id;

    @Column(name = "group_name", unique = true)
    @JsonView(JsonViewFilter.BasicInfo.class)
    @NotNull
    @Length(min = 3, max = 20)
    private String groupName;

    @OneToMany(mappedBy = "daycareGroup", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"daycareGroup", "assignedOptions"})
    @OrderBy(value = "lastName ASC, firstName ASC")
    private Set<Child> children = new HashSet<>();

    @OneToOne
    @JsonView(JsonViewFilter.BasicInfo.class)
    @JoinColumn(name = "group_supervisor_id")
    @JsonIgnoreProperties("daycareGroup")
    private AppUser groupSupervisor;

    public DaycareGroup(String groupName) {
        this.groupName = groupName;
    }
}

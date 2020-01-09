package jedrekp.daycarecateringbillgenerator.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import jedrekp.daycarecateringbillgenerator.utility.JsonViewFilter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "daycare_group")
@Getter
@Setter
public class DaycareGroup {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(JsonViewFilter.BasicInfo.class)
    private long id;

    @Column(name = "group_name", unique = true)
    @JsonView(JsonViewFilter.BasicInfo.class)
    @NotNull
    private String groupName;

    @OneToMany(mappedBy = "daycareGroup", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"daycareGroup", "assignedOptions"})
    @OrderBy(value = "lastName ASC, firstName ASC")
    private Set<Child> children = new HashSet<>();
}

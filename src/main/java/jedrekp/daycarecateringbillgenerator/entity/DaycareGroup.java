package jedrekp.daycarecateringbillgenerator.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import jedrekp.daycarecateringbillgenerator.utility.JsonViewFilter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "daycare_group")
@Getter
@Setter
@NoArgsConstructor
public class DaycareGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(JsonViewFilter.BasicInfo.class)
    private long id;

    @Column(name = "group_name", unique = true)
    @JsonView(JsonViewFilter.BasicInfo.class)
    private String groupName;

    @OneToMany(mappedBy = "daycareGroup", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"daycareGroup", "assignedOptions"})
    @OrderBy(value = "lastName ASC, firstName ASC")
    private Set<Child> children = new HashSet<>();
}

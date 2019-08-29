package jedrekp.daycarecateringbillgenerator.Entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import jedrekp.daycarecateringbillgenerator.Utility.JsonViewFilter;
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
    private Long id;

    @Column(name = "group_name")
    @JsonView(JsonViewFilter.BasicInfo.class)
    private String groupName;

    @OneToMany(mappedBy = "daycareGroup", fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE})
    @JsonIgnoreProperties({"daycareGroup"})
    private Set<Child> children = new HashSet<>();
}

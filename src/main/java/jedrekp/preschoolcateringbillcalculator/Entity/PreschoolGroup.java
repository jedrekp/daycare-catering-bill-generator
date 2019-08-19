package jedrekp.preschoolcateringbillcalculator.Entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import jedrekp.preschoolcateringbillcalculator.Utility.JsonViewFilter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "preschool_group")
@Getter
@Setter
@NoArgsConstructor
public class PreschoolGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(JsonViewFilter.BasicInfo.class)
    private Long id;

    @Column(name = "group_name")
    @JsonView(JsonViewFilter.BasicInfo.class)
    private String groupName;

    @OneToMany(mappedBy = "preschoolGroup", fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE})
    @JsonIgnoreProperties({"preschoolGroup"})
    private Set<Child> children = new HashSet<>();
}

package jedrekp.preschoolcateringbillcalculator.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import jedrekp.preschoolcateringbillcalculator.Utility.JsonViewFilter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "child")
@Getter
@Setter
@NoArgsConstructor
public class Child {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(JsonViewFilter.BasicInfo.class)
    private Long id;

    @Column(name = "first_name")
    @JsonView(JsonViewFilter.BasicInfo.class)
    private String firstName;

    @Column(name = "last_name")
    @JsonView(JsonViewFilter.BasicInfo.class)
    private String lastName;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE})
    @JoinColumn(name = "preschool_group_id")
    @JsonIgnoreProperties({"children"})
    @JsonView({JsonViewFilter.WithGroup.class})
    private PreschoolGroup preschoolGroup;

    @OneToMany(mappedBy = "child", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JsonIgnoreProperties({"id", "child", "diet.children"})
    @JsonView({JsonViewFilter.WithDiets.class})
    @OrderBy("effective_date DESC")
    private Set<ChildDiet> chosenDiets = new LinkedHashSet<>();

}

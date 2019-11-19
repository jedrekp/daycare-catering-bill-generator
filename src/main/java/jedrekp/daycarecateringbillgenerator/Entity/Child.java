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
@Table(name = "child",
        uniqueConstraints = @UniqueConstraint(columnNames = {"first_name", "last_name"}))
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

    @Column(name = "parent_email")
    @JsonView(JsonViewFilter.BasicInfo.class)
    private String parentEmail;

    @Column(name = "archived")
    @JsonView(JsonViewFilter.BasicInfo.class)
    private boolean archived;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daycare_group_id")
    @JsonIgnoreProperties({"children"})
    private DaycareGroup daycareGroup;

    @OneToMany(mappedBy = "child", fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JsonIgnoreProperties({"child", "cateringOption.children"})
    @JsonView(JsonViewFilter.WithAssignedOptions.class)
    @OrderBy(value = "effectiveDate DESC")
    private Set<AssignedOption> assignedOptions = new HashSet<>();

    @OneToMany(mappedBy = "child", fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JsonIgnoreProperties({"child", "dailyCateringOrders"})
    @OrderBy(value = "year DESC, month DESC")
    private Set<CateringBill> cateringBills = new HashSet<>();

}

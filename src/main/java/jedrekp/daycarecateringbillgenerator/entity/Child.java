package jedrekp.daycarecateringbillgenerator.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import jedrekp.daycarecateringbillgenerator.utility.JsonViewFilter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
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

    @Column(name = "parent_email")
    @JsonView(JsonViewFilter.BasicInfo.class)
    @NotNull
    @Email
    private String parentEmail;

    @JsonView(JsonViewFilter.BasicInfo.class)
    @NotNull
    private boolean archived;

    @JsonView(JsonViewFilter.BasicInfo.class)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "daycare_group_id")
    @JsonIgnoreProperties({"children"})
    private DaycareGroup daycareGroup;

    @OneToMany(mappedBy = "child", fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JsonIgnoreProperties({"child"})
    @OrderBy(value = "effectiveDate DESC")
    private Set<AssignedOption> assignedOptions = new HashSet<>();

    @OneToMany(mappedBy = "child", fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JsonIgnore
    @OrderBy(value = "year DESC, month DESC")
    private Set<CateringBill> cateringBills = new HashSet<>();

    public Child(
            @NotNull @Length(min = 2, max = 20) String firstName,
            @NotNull @Length(min = 2, max = 20) String lastName,
            @NotNull @Email String parentEmail,
            @NotNull boolean archived) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.parentEmail = parentEmail;
        this.archived = archived;
    }
}

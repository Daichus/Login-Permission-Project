package login.permission.project.classes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="Employee")
public class Employee {


    @Id
    @Column(name = "employee_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int employee_id;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "phone_number")
    private String phoneNumber;


    @Column(name = "status_id")
    private Integer status_id;

    @ManyToOne
    @JoinColumn(name = "status_id", insertable = false, updatable = false)
    private Status employeeStatus;

    @ManyToMany
    @JoinTable(
            name = "EmpPositionMap",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "position_id")
    )
    private Set<Position> positions;

    @OneToMany(mappedBy = "employee")
    private Set<LoginRecord> loginRecords;

}

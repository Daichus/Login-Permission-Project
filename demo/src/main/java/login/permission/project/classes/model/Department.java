package login.permission.project.classes.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="Department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="department_id")
    private int department_id;

    @Column(name="department_name")
    private String department_name;

    @Column(name="department_code")
    private String department_code;

    @OneToMany(mappedBy = "department")
    private Set<Unit> units;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "department")
    private Set<Employee> employees;
}

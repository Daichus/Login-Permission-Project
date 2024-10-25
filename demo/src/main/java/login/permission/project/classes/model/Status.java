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
@Table(name="Status")
public class Status {

    @Id
    private int status_id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "employeeStatus")
    private Set<Employee> employees;
}

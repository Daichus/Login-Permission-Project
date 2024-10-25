package login.permission.project.classes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="EmpPositionMap")
public class EmpPositionMap {

    @Id
    private int empPositionMap_id;

    @Column(name = "employee_id")
    private Integer employee_id;

    @Column(name = "position_id")
    private Integer position_id;

    @ManyToOne
    @JoinColumn(name = "employee_id", insertable = false, updatable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "position_id", insertable = false, updatable = false)
    private Position position;

}







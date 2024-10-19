package login.permission.project.classes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    @Column(name="EmpPositionMap_id")
    private int EmpPositionMap_id;

    @Column(name="employee_id")
    private int employee_id;

    @Column(name="position_id")
    private int position_id;

}







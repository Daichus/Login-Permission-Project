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
@Table(name="Unit")
public class Unit {

    //非自動添加
    @Id
    @Column(name="unit_id")
    private int unit_id;

    @Column(name="unit_name")
    private String unit_name;

    @Column(name="unit_code")
    private String unit_code;

    @Column(name = "department_id")
    private Integer department_id;

    @ManyToOne
    @JoinColumn(name = "department_id", insertable = false, updatable = false)
    private Department department;

    @OneToMany(mappedBy = "unit")
    private Set<Position> positions;
}

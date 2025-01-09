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
@Table(name="Position")
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="position_id")
    private int position_id;


    @Column(name="position")
    private String position;

//    @Column(name = "unit_id")
//    private Integer unit_id;

    @ManyToOne
    @JoinColumn(name = "unit_id" )
    private Unit unit;

    @OneToMany(mappedBy = "position", fetch = FetchType.LAZY)
    private Set<Employee> employees;

}

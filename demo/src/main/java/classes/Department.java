package classes;


import jakarta.persistence.*;
import lombok.*;

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


}

package flu.demo.table_class;


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
@Table(name="Department")
public class Department {

    @Id
    @Column(name="department_id")
    private int department_id;

    @Column(name="department_name")
    private String department_name;

    @Column(name="department_code")
    private String department_code;




}

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
@Table(name="Unit")
public class Unit {

    @Id
    @Column(name="unit_id")
    private int unit_id;

    @Column(name="unit_name")
    private String unit_name;

    @Column(name="unit_code")
    private String unit_code;

    @Column(name="department_id")
    private int department_id;
}

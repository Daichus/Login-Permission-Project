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
@Table(name="Position")
public class Position {

    @Id
    @Column(name="position_id")
    private int position_id;


    @Column(name="position")
    private String position;


    @Column(name="unit_id")
    private int unit_id;

}

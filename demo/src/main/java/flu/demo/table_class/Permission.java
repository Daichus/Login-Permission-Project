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
@Table(name="Permission")
public class Permission {

    @Id
    @Column(name="permission_id")
    private int permission_id;

    @Column(name="permission_name")
    private String permission_name;

    @Column(name="description")
    private String description;

}

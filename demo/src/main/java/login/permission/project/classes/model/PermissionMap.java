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
@Table(name="PermissionMap")
public class PermissionMap {

    @Id
    @Column(name="permissionMap_id")
    private int permissionMap_id;


    @Column(name="position_id")
    private int position_id;

    @Column(name="permission_id")
    private int permission_id;




}

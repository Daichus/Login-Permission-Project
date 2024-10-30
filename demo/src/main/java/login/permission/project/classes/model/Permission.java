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
@Table(name="Permission")
public class Permission {

    @Id
    @Column(name = "permission_id")
    private int permission_id;

    @Column(name = "permission_name")
    private String permission_name;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "permissions")
    private Set<Position> positions;


}

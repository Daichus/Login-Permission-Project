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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private int permission_id;

    @Column(name = "permission_name")
    private String permission_name;

    @Column(name="category")
    private String category;

    @Column(name="permission_code")
    private String permission_code;



}

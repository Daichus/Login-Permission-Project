package login.permission.project.classes.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="Role")
public class Role {

    /**
     * 角色id,作為對對多
     */
    @Id
    @Column(name="role_id")
    private int  role_id;

    /**
     * 僅供標示角色名稱用
     */
    @Column(name="Role")
    private String role;

    /**
     * 由jpa自動生成關聯表,對應permission表
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "RolePermissionMap",
               joinColumns = @JoinColumn(name = "role_id"),
               inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions = new HashSet<>();





}

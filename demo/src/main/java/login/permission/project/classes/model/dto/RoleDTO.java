package login.permission.project.classes.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleDTO {

    private String role_id;

    private String roleName;

    private String [] permission_id;

    private String [] requestPermissionCode;
}

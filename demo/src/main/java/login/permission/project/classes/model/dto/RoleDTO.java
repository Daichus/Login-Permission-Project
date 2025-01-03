package login.permission.project.classes.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleDTO {

    private int role_id;

    private String roleName;

    private String [] permission_id;


}

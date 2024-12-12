package login.permission.project.classes.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeRoleDto {

    private int employee_id;

    private String [] roleIds;

}

package login.permission.project.classes.model.dto;


import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeRoleDto {

    private int employee_id;

    private String name;

    private String email;

    private String phoneNumber;

    private String [] roleIds;



}

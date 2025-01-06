package login.permission.project.classes.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeUpdateDto {

    private int employee_id;

    private String name;

    private String email;

    private String phoneNumber;

    private int unit_id;

    private int department_id;

    private int position_id;

    private int status_id;

    private String [] roleIds;
}

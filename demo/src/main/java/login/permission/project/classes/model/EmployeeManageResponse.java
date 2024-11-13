package login.permission.project.classes.model;



import lombok.Data;


@Data
public class EmployeeManageResponse {
    public EmployeeManageResponse(int employee_id, String name, String email, String phoneNumber, String departmentName, int permission_id,String unitName, String positionName, String statusName) {
        this.employee_id = employee_id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.departmentName = departmentName;
        this.unitName = unitName;
        this.positionName = positionName;
        this.permission_id = permission_id;
        this.statusName = statusName;
    }

    private int employee_id;
    private String name;
    private String email;
    private String phoneNumber;
    private String departmentName;
    private String unitName;
    private int permission_id;
    private String positionName;
    private String statusName;







}

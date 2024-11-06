package login.permission.project.classes.model;



import lombok.Data;


@Data
public class EmployeeManageResponse {
    public EmployeeManageResponse(int employee_id, String name, String email, String phoneNumber, String departmentName, String unitName, String positionName, String statusName) {
        this.employee_id = employee_id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.departmentName = departmentName;
        this.unitName = unitName;
        this.positionName = positionName;
        this.statusName = statusName;
    }

    private int employee_id;
    private String name;
    private String email;
    private String phoneNumber;
    private String departmentName;
    private String unitName;
    private String positionName;
    private String statusName;







}

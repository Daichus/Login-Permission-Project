package service;


import classes.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.DepartmentRepository;

@Service
public class DepartmentService {

    @Autowired
    DepartmentRepository dr;

    public String addDepartment(Department department) {
        dr.save(department);
        return String.format("新增部門成功\n部門名稱: %s\n部門id: %s\n部門代號: %s",
                department.getDepartment_name(),
                department.getDepartment_id(),
                department.getDepartment_code());
    }
}

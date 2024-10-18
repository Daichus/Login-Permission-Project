package login.permission.project.classes.service;


import login.permission.project.classes.model.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import login.permission.project.classes.repository.DepartmentRepository;

import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    DepartmentRepository dr;

    public List<Department> getAllDepartments() {
        return dr.findAll();
    }

    public String addDepartment(Department department) {
        dr.save(department);
        return String.format("新增部門成功\n部門名稱: %s\n部門id: %s\n部門代號: %s",
                department.getDepartment_name(),
                department.getDepartment_id(),
                department.getDepartment_code());
    }
}

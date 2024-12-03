package login.permission.project.classes.service;

import java.util.List;
import login.permission.project.classes.model.Department;
import login.permission.project.classes.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * DepartmentService.
 */
@Service
public class DepartmentService {

  @Autowired
  DepartmentRepository dr;

  public List<Department> getAllDepartments() {
    return dr.findAll();
  }

  /**
   * 新增部門.
   *
   * @param department create
   * @return Add New Department
   */
  public String addDepartment(Department department) {
    dr.save(department);
    return String.format("新增部門成功\n部門名稱: %s\n部門id: %s\n部門代號: %s",
            department.getDepartment_name(),
            department.getDepartment_id(),
            department.getDepartment_code());
  }

  /**
   * 更新部門資訊.
   *
   * @param department Edit
   * @return After edit, New Department Information
   */
  public String updateDepartment(Department department) {
    if (department != null) {
      dr.save(department);
      return "修改部門資訊完成";
    } else {
      return "更新部門資訊失敗";
    }
  }

  public String deleteDepartment(int id) {
    dr.deleteById(id);
    return "刪除部門成功";
  }

}

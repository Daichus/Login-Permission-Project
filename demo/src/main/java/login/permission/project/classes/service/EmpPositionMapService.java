package login.permission.project.classes.service;

import java.util.List;
import login.permission.project.classes.model.EmpPositionMap;
import login.permission.project.classes.repository.EmpPositionMapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing employee position mappings.
 */
@Service
public class EmpPositionMapService {

  @Autowired
  EmpPositionMapRepository dr;

  /**
   * Retrieves all employee position mappings.
   *
   * @return List of all EmpPositionMap objects
   */
  public List<EmpPositionMap> getAllEmpPositionMaps() {
    return dr.findAll();
  }

  /**
   * Creates a new employee position mapping.
   *
   * @param empPositionMap The EmpPositionMap object to add
   * @return Success message with mapping details
   */
  public String addEmpPositionMap(EmpPositionMap empPositionMap) {
    dr.save(empPositionMap);
    return String.format("新增職位設定成功\n職位設定代號: %s\n員工代號: %s\n職位代號: %s",
            empPositionMap.getEmpPositionMapId(),
            empPositionMap.getEmployeeId(),
            empPositionMap.getPositionId());
  }

  /**
   * Updates an existing employee position mapping.
   *
   * @param empPositionMap The EmpPositionMap object with updated info
   * @return Success or failure message
   */
  public String updateEmpPositionMap(EmpPositionMap empPositionMap) {
    if (empPositionMap != null) {
      dr.save(empPositionMap);
      return "修改職位設定資訊完成";
    } else {
      return "更新職位設定資訊失敗";
    }
  }

  /**
   * Deletes an employee position mapping.
   *
   * @param id The ID of the EmpPositionMap to delete
   * @return Success message
   */
  public String deleteEmpPositionMap(int id) {
    dr.deleteById(id);
    return "刪除職位設定成功";
  }
}
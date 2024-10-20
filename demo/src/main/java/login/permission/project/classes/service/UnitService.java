package login.permission.project.classes.service;

import login.permission.project.classes.model.Unit;
import login.permission.project.classes.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnitService {

  @Autowired
  UnitRepository unitRepository;

  //get
  public List<Unit> getAllUnit() {
    return unitRepository.findAll();
  }

  //post
  public String addUnit(Unit unit) {
    unitRepository.save(unit);
    return String.format("新增科別成功\n科別id: %s\n科別名稱: %s\n科別代碼: %s\n部門id: %s",
            unit.getUnit_id(),
            unit.getUnit_name(),
            unit.getUnit_code(),
            unit.getDepartment_id());
  }

  //put
  public String updateUnit(Unit unit) {
    if(unit != null) {
      unitRepository.save(unit);
      return "修改科別資訊完成";
    }
    return "更新科別資訊失敗";
  }

  //delete
  public String deleteUnit(int id) {
    unitRepository.deleteById(id);
    return "刪除科別成功";
  }


}

package login.permission.project.classes.service;

import login.permission.project.classes.model.Unit;
import login.permission.project.classes.model.dto.UnitDto;
import login.permission.project.classes.model.util.ResponseUtil;
import login.permission.project.classes.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UnitService {

  @Autowired
  UnitRepository unitRepository;

  //get
  public ResponseEntity<?> getAllUnit() {
    List<Unit> allUnit = unitRepository.findAll();
    return ResponseUtil.success("獲取科別成功",allUnit);
  }

  //post
  public ResponseEntity<?> addUnit(UnitDto dto) {
    Unit unit = new Unit();
    int maxUnitId = unitRepository.findMaxId();
    int newUnitId =  maxUnitId + 1 ;
    unit.setUnit_id(newUnitId);
    unit.setUnit_name(dto.getUnit_name());
    unit.setUnit_code(dto.getUnit_code());
    unit.setDepartment_id(dto.getDepartment_id());
    unitRepository.save(unit);
    return ResponseUtil.success("添加科別成功",null);
  }

  //put
  public ResponseEntity<?> updateUnit(UnitDto dto) {
    Optional<Unit> unitOptional= unitRepository.findById(dto.getUnit_id());
    if(unitOptional.isPresent()) {
      Unit unit = unitOptional.get();
      unit.setUnit_code(dto.getUnit_code());
      unit.setUnit_name(dto.getUnit_name());
      unit.setDepartment_id(dto.getDepartment_id());
      unitRepository.save(unit);
      return ResponseUtil.success("修改科別資訊完成",null);
    }
    return ResponseUtil.error("找不到指定的科別", HttpStatus.NOT_FOUND);
  }

  //delete
  public ResponseEntity<?> deleteUnit(int id) {
    unitRepository.deleteById(id);
    return ResponseUtil.success("刪除科別完成",null);
  }


}

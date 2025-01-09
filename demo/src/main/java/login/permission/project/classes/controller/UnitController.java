package login.permission.project.classes.controller;

import login.permission.project.classes.model.Unit;
import login.permission.project.classes.model.dto.UnitDto;
import login.permission.project.classes.service.UnitService;
import login.permission.project.classes.annotation.LogOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/unit/test")
public class UnitController {

  @Autowired
  UnitService unitService;

  @LogOperation(module = "科別管理", operation = "查詢", description = "查詢所有科別")
  @GetMapping("/get")
  @PreAuthorize("hasAuthority('unit_mgt_read')")
  public ResponseEntity<?> getAllUnits() {
    return unitService.getAllUnit();
  }

  @LogOperation(module = "科別管理", operation = "新增", description = "新增科別")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('unit_mgt_create')")
  public ResponseEntity<?> addUnit(@RequestBody UnitDto dto) {
    return unitService.addUnit(dto);
  }

  @LogOperation(module = "科別管理", operation = "修改", description = "修改科別")
  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('unit_mgt_update')")
  public ResponseEntity<?> updateUnit(@RequestBody UnitDto dto) {
    return unitService.updateUnit(dto);
  }

  @LogOperation(module = "科別管理", operation = "刪除", description = "刪除科別")
  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasAuthority('unit_mgt_delete')")
  public ResponseEntity<?> deleteUnit(@PathVariable int id){
    return unitService.deleteUnit(id);
  }


}

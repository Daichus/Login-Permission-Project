package login.permission.project.classes.controller;

import login.permission.project.classes.model.Unit;
import login.permission.project.classes.model.dto.UnitDto;
import login.permission.project.classes.service.UnitService;
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

  @GetMapping("/get")
  @PreAuthorize("hasAuthority('unit_mgt_read')")
  public ResponseEntity<?> getAllUnits() {
    return unitService.getAllUnit();
  }

  @PostMapping("/add")
  @PreAuthorize("hasAuthority('unit_mgt_create')")
  public ResponseEntity<?> addUnit(@RequestBody UnitDto dto) {
    return unitService.addUnit(dto);
  }

  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('unit_mgt_update')")
  public ResponseEntity<?> updateUnit(@RequestBody UnitDto dto) {
    return unitService.updateUnit(dto);
  }

  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasAuthority('unit_mgt_delete')")
  public ResponseEntity<?> deleteUnit(@PathVariable int id){
    return unitService.deleteUnit(id);
  }


}

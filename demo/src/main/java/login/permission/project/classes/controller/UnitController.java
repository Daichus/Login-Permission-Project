package login.permission.project.classes.controller;

import login.permission.project.classes.model.Unit;
import login.permission.project.classes.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/unit/test")
public class UnitController {

  @Autowired
  UnitService unitService;

  @GetMapping("/get")
  public List<Unit> getAllUnits() {
    return unitService.getAllUnit();
  }

  @PostMapping("/add")
  public String addUnit(@RequestBody Unit unit) {
    return unitService.addUnit(unit);
  }

  @PutMapping("/edit")
  public String updateUnit(@RequestBody Unit unit) {
    return unitService.updateUnit(unit);
  }

  @DeleteMapping("/delete/{id}")
  public String deleteUnit(@PathVariable int id){
    return unitService.deleteUnit(id);
  }


}

package login.permission.project.classes.controller;

import java.util.List;
import login.permission.project.classes.model.EmpPositionMap;
import login.permission.project.classes.service.EmpPositionMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for managing employee position mappings.
 */
@RestController
@RequestMapping("/api/empPositionMap")
public class EmpPositionMapController {

  @Autowired
  EmpPositionMapService eps;

  /**
   * Gets all employee position mappings.
   *
   * @return List of all EmpPositionMap objects
   */
  @GetMapping("/get")
  public List<EmpPositionMap> getAllEmpPositionMaps() {
    return eps.getAllEmpPositionMaps();
  }

  /**
   * Creates a new employee position mapping.
   *
   * @param empPositionMap The EmpPositionMap object to add
   * @return String indicating the operation result
   */
  @PostMapping("/add")
  public String addEmpPositionMap(@RequestBody EmpPositionMap empPositionMap) {
    return eps.addEmpPositionMap(empPositionMap);
  }

  /**
   * Updates an existing employee position mapping.
   *
   * @param empPositionMap The EmpPositionMap object with updated info
   * @return String indicating the operation result
   */
  @PutMapping("/edit")
  public String updateEmpPositionMap(@RequestBody EmpPositionMap empPositionMap) {
    return eps.updateEmpPositionMap(empPositionMap);
  }

  /**
   * Deletes an employee position mapping by ID.
   *
   * @param id The ID of the EmpPositionMap to delete
   * @return String indicating the operation result
   */
  @DeleteMapping("/delete/{id}")
  public String deleteEmpPositionMap(@PathVariable int id) {
    return eps.deleteEmpPositionMap(id);
  }
}
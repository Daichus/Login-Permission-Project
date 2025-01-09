package login.permission.project.classes.controller;

import login.permission.project.classes.annotation.LogOperation;
import login.permission.project.classes.model.EmpPositionMap;
import login.permission.project.classes.service.EmpPositionMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empPositionMap")
public class EmpPositionMapController {

    @Autowired
    EmpPositionMapService eps;

    // 獲取所有職位設定
    @LogOperation(module = "職位設定", operation = "查詢", description = "查詢所有職位設定")
    @GetMapping("/get")
    public List<EmpPositionMap> getAllEmpPositionMaps() {
        return eps.getAllEmpPositionMaps();
    }

    // 新增職位設定
    @LogOperation(module = "職位設定", operation = "新增", description = "新增職位設定")
    @PostMapping("/add")
    public String addEmpPositionMap(@RequestBody EmpPositionMap empPositionMap) {
        return eps.addEmpPositionMap(empPositionMap);
    }

    // 修改職位設定
    @LogOperation(module = "職位設定", operation = "修改", description = "修改職位設定")
    @PutMapping("/edit")
    public String updateEmpPositionMap(@RequestBody EmpPositionMap empPositionMap) {
        return eps.updateEmpPositionMap(empPositionMap);
    }

    // 刪除職位設定
    @LogOperation(module = "職位設定", operation = "刪除", description = "刪除職位設定")
    @DeleteMapping("/delete/{id}")
    public String deleteEmpPositionMap(@PathVariable int id) {
        return eps.deleteEmpPositionMap(id);
    }

}








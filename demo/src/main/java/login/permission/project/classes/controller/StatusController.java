package login.permission.project.classes.controller;

import login.permission.project.classes.model.Status;
import login.permission.project.classes.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import login.permission.project.classes.annotation.LogOperation;

import java.util.List;

@RestController
@RequestMapping("/status/test")
public class StatusController {

    @Autowired
    StatusService ss;

    @LogOperation(module = "狀態管理", operation = "查詢", description = "查詢所有狀態")
    @GetMapping("/get")
    public List<Status> getAllStatus() {
        return ss.getAllStatus();
    }

    @LogOperation(module = "狀態管理", operation = "新增", description = "新增狀態")
    @PostMapping("/add")
    public String addStatus(@RequestBody Status status) {
        return ss.addStatus(status);
    }

    @LogOperation(module = "狀態管理", operation = "修改", description = "修改狀態")
    @PutMapping("/edit")
    public String updateStatus (@RequestBody Status status) {
        return ss.updateStatus(status);
    }

    @LogOperation(module = "狀態管理", operation = "刪除", description = "刪除狀態")
    @DeleteMapping("/delete/{id}")
    public String deleteStatus(@PathVariable int id) {
        return ss.deleteStatus(id);
    }
}

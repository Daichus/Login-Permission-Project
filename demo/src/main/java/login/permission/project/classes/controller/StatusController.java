package login.permission.project.classes.controller;



import login.permission.project.classes.model.Status;
import login.permission.project.classes.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/status/test")
public class StatusController {

    @Autowired
    StatusService ss;

    @GetMapping("/get")
    public List<Status> getAllStatus() {
        return ss.getAllStatus();
    }

    @PostMapping("/add")
    public String addStatus(@RequestBody Status status) {
        return ss.addStatus(status);
    }

    @PutMapping("/edit")
    public String updateStatus (@RequestBody Status status) {
        return ss.updateStatus(status);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteStatus(@PathVariable int id) {
        return ss.deleteStatus(id);
    }
}

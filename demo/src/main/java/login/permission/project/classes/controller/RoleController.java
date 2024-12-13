package login.permission.project.classes.controller;


import jakarta.servlet.http.HttpServletRequest;
import login.permission.project.classes.model.dto.RoleDTO;
import login.permission.project.classes.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    RoleService roleService;

    @GetMapping  ("/getAll")
    public ResponseEntity<?> getAllRole () {
        return roleService.getAllRole();
    }

    @PostMapping ("/create")
    public ResponseEntity<?> createRole(@PathVariable RoleDTO roleDto, HttpServletRequest request) {
        return roleService.createRole(roleDto,request);
    }

    @PutMapping("/edit")
    public ResponseEntity<?> updateRole (@PathVariable  RoleDTO roleDto, HttpServletRequest request) {
        return roleService.updateRole( roleDto,   request);
    }

}

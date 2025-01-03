package login.permission.project.classes.controller;


import jakarta.servlet.http.HttpServletRequest;
import login.permission.project.classes.model.dto.RoleDTO;
import login.permission.project.classes.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('role_mgt_create')")
    public ResponseEntity<?> createRole(@RequestBody RoleDTO roleDto, HttpServletRequest request) {
        return roleService.createRole(roleDto,request);
    }

    @PutMapping("/edit")
    @PreAuthorize("hasAuthority('role_mgt_update')")
    public ResponseEntity<?> updateRole (@RequestBody  RoleDTO roleDto, HttpServletRequest request) {
        return roleService.updateRole( roleDto, request);
    }

    @DeleteMapping("/delete/{role_id}")
    @PreAuthorize("hasAuthority('role_mgt_delete')")
    public ResponseEntity<?> deleteRole (@PathVariable int role_id) {
        return roleService.deleteRole(role_id);
    }

}

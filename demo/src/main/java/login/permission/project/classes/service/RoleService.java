package login.permission.project.classes.service;


import login.permission.project.classes.model.Role;
import login.permission.project.classes.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;


    public List<Role> getAllRole() {
        return roleRepository.findAll();
    }












}

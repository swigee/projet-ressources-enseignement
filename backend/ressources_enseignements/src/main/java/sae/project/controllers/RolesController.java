package sae.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sae.project.model.Role;
import sae.project.services.RoleService;
import java.util.List;


@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "http://localhost:4200")
public class RolesController {

    @Autowired
    private RoleService roleService;

    @GetMapping(path = "/list", produces = { "application/json" })
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

}

package sae.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sae.project.model.Permission;
import sae.project.model.Role;
import sae.project.services.RoleService;

import java.util.List;
import java.util.Map;
import java.util.Set;

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

    @GetMapping(path = "/{id}", produces = { "application/json" })
    public Role getRoleById(@PathVariable int id) {
        return roleService.getRoleById(id);
    }

    @PostMapping(consumes = { "application/json" }, produces = { "application/json" })
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        Role created = roleService.createRole(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping(path = "/{id}", consumes = { "application/json" }, produces = { "application/json" })
    public Role updateRole(@PathVariable int id, @RequestBody Role role) {
        return roleService.updateRole(id, role);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable int id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/{id}/permissions", produces = { "application/json" })
    public Set<Permission> getRolePermissions(@PathVariable int id) {
        return roleService.getRolePermissions(id);
    }

    @PutMapping(path = "/{id}/permissions", consumes = { "application/json" }, produces = { "application/json" })
    public Role setRolePermissions(@PathVariable int id, @RequestBody List<Integer> permissionIds) {
        return roleService.setRolePermissions(id, permissionIds);
    }

    @PostMapping(path = "/{id}/permissions", consumes = { "application/json" }, produces = { "application/json" })
    public Role addRolePermissions(@PathVariable int id, @RequestBody List<Integer> permissionIds) {
        return roleService.addRolePermissions(id, permissionIds);
    }

    @DeleteMapping(path = "/{id}/permissions/{permissionId}", produces = { "application/json" })
    public Role removeRolePermission(@PathVariable int id, @PathVariable int permissionId) {
        return roleService.removeRolePermission(id, permissionId);
    }

    @GetMapping(path = "/{id}/users/count", produces = { "application/json" })
    public long countUsersForRole(@PathVariable int id) {
        return roleService.countUsersByRoleId(id);
    }

    @GetMapping(path = "/users/count", produces = { "application/json" })
    public Map<Integer, Long> countUsersForAllRoles() {
        return roleService.countUsersForAllRoles();
    }

    @GetMapping(path = "/{id}/permissions/count", produces = { "application/json" })
    public long countPermissionsForRole(@PathVariable int id) {
        return roleService.countPermissionsByRoleId(id);
    }

    @GetMapping(path = "/permissions/count", produces = { "application/json" })
    public Map<Integer, Long> countPermissionsForAllRoles() {
        return roleService.countPermissionsForAllRoles();
    }
}

package sae.project.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Roles", description = "API for managing roles and their permissions")
public class RolesController {

    @Autowired
    private RoleService roleService;

    @GetMapping(path = "/list", produces = { "application/json" })
    @Operation(summary = "Get all roles")
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping(path = "/{id}", produces = { "application/json" })
    @Operation(summary = "Get a role by ID")
    public Role getRoleById(@PathVariable int id) {
        return roleService.getRoleById(id);
    }

    @PostMapping(consumes = { "application/json" }, produces = { "application/json" })
    @Operation(summary = "Create a new role")
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        Role created = roleService.createRole(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping(path = "/{id}", consumes = { "application/json" }, produces = { "application/json" })
    @Operation(summary = "Update a role by ID")
    public Role updateRole(@PathVariable int id, @RequestBody Role role) {
        return roleService.updateRole(id, role);
    }

    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Delete a role by ID")
    public ResponseEntity<Void> deleteRole(@PathVariable int id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/{id}/permissions", produces = { "application/json" })
    @Operation(summary = "Get all permissions assigned to a role")
    public Set<Permission> getRolePermissions(@PathVariable int id) {
        return roleService.getRolePermissions(id);
    }

    @PutMapping(path = "/{id}/permissions", consumes = { "application/json" }, produces = { "application/json" })
    @Operation(summary = "Replace all permissions of a role")
    public Role setRolePermissions(@PathVariable int id, @RequestBody List<Integer> permissionIds) {
        return roleService.setRolePermissions(id, permissionIds);
    }

    @PostMapping(path = "/{id}/permissions", consumes = { "application/json" }, produces = { "application/json" })
    @Operation(summary = "Add permissions to a role")
    public Role addRolePermissions(@PathVariable int id, @RequestBody List<Integer> permissionIds) {
        return roleService.addRolePermissions(id, permissionIds);
    }

    @DeleteMapping(path = "/{id}/permissions/{permissionId}", produces = { "application/json" })
    @Operation(summary = "Remove a specific permission from a role")
    public Role removeRolePermission(@PathVariable int id, @PathVariable int permissionId) {
        return roleService.removeRolePermission(id, permissionId);
    }

    @GetMapping(path = "/{id}/users/count", produces = { "application/json" })
    @Operation(summary = "Count users assigned to a specific role")
    public long countUsersForRole(@PathVariable int id) {
        return roleService.countUsersByRoleId(id);
    }

    @GetMapping(path = "/users/count", produces = { "application/json" })
    @Operation(summary = "Count users per role for all roles")
    public Map<Integer, Long> countUsersForAllRoles() {
        return roleService.countUsersForAllRoles();
    }

    @GetMapping(path = "/{id}/permissions/count", produces = { "application/json" })
    @Operation(summary = "Count permissions assigned to a specific role")
    public long countPermissionsForRole(@PathVariable int id) {
        return roleService.countPermissionsByRoleId(id);
    }

    @GetMapping(path = "/permissions/count", produces = { "application/json" })
    @Operation(summary = "Count permissions per role for all roles")
    public Map<Integer, Long> countPermissionsForAllRoles() {
        return roleService.countPermissionsForAllRoles();
    }
}

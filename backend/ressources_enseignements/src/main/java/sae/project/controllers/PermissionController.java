package sae.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sae.project.model.Permission;
import sae.project.services.PermissionService;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@CrossOrigin(origins = "http://localhost:4200")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping(path = "/list", produces = {"application/json"})
    public List<Permission> getAllPermissions() {
        return permissionService.getAllPermissions();
    }

    @GetMapping(path = "/{id}", produces = {"application/json"})
    public Permission getPermissionById(@PathVariable int id) {
        return permissionService.getPermissionById(id);
    }

    @PostMapping(consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Permission> createPermission(@RequestBody Permission permission) {
        Permission created = permissionService.createPermission(permission);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping(path = "/{id}", consumes = {"application/json"}, produces = {"application/json"})
    public Permission updatePermission(@PathVariable int id, @RequestBody Permission permission) {
        return permissionService.updatePermission(id, permission);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable int id) {
        permissionService.deletePermission(id);
        return ResponseEntity.noContent().build();
    }
}


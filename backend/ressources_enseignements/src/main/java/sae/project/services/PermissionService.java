package sae.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sae.project.model.Permission;
import sae.project.repositories.PermissionRepository;

import java.util.List;

@Service
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    public Permission getPermissionById(int id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
    }

    public Permission createPermission(Permission permission) {
        permission.setId(null);
        return permissionRepository.save(permission);
    }

    public Permission updatePermission(int id, Permission permission) {
        Permission existing = getPermissionById(id);
        existing.setName(permission.getName());
        existing.setDescription(permission.getDescription());
        return permissionRepository.save(existing);
    }

    public void deletePermission(int id) {
        permissionRepository.deleteById(id);
    }
}


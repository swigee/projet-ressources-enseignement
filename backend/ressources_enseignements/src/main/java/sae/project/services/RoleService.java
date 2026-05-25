package sae.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sae.project.model.Permission;
import sae.project.model.Role;
import sae.project.repositories.PermissionRepository;
import sae.project.repositories.RoleRepository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleById(int id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
    }

    public Role createRole(Role role) {
        role.setId(null);
        return roleRepository.save(role);
    }

    public Role updateRole(int id, Role role) {
        Role existing = getRoleById(id);
        existing.setName(role.getName());
        existing.setIsActive(role.getIsActive());
        existing.setColorHex(role.getColorHex());
        existing.setSlug(role.getSlug());
        return roleRepository.save(existing);
    }

    public void deleteRole(int id) {
        roleRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Set<Permission> getRolePermissions(int roleId) {
        Role role = getRoleById(roleId);
        // permissions est @JsonIgnore mais on peut le renvoyer via endpoint dédié
        return role.getPermissions();
    }

    @Transactional
    public Role setRolePermissions(int roleId, List<Integer> permissionIds) {
        Role role = getRoleById(roleId);
        Set<Permission> newPermissions = new HashSet<>(permissionRepository.findAllById(permissionIds));
        role.setPermissions(newPermissions);
        return roleRepository.save(role);
    }

    @Transactional
    public Role addRolePermissions(int roleId, List<Integer> permissionIds) {
        Role role = getRoleById(roleId);
        Set<Permission> current = role.getPermissions();
        if (current == null) {
            current = new HashSet<>();
            role.setPermissions(current);
        }
        current.addAll(permissionRepository.findAllById(permissionIds));
        return roleRepository.save(role);
    }

    @Transactional
    public Role removeRolePermission(int roleId, int permissionId) {
        Role role = getRoleById(roleId);
        Set<Permission> current = role.getPermissions();
        if (current != null) {
            current.removeIf(p -> p.getId() != null && p.getId() == permissionId);
        }
        return roleRepository.save(role);
    }

    public long countUsersByRoleId(int roleId) {
        return roleRepository.countUsersByRoleId(roleId);
    }

    public Map<Integer, Long> countUsersForAllRoles() {
        Map<Integer, Long> result = new HashMap<>();
        for (Object[] row : roleRepository.countUsersForAllRoles()) {
            Integer roleId = (Integer) row[0];
            Long count = (Long) row[1];
            result.put(roleId, count);
        }
        return result;
    }

    public long countPermissionsByRoleId(int roleId) {
        return roleRepository.countPermissionsByRoleId(roleId);
    }

    public Map<Integer, Long> countPermissionsForAllRoles() {
        Map<Integer, Long> result = new HashMap<>();
        for (Object[] row : roleRepository.countPermissionsForAllRoles()) {
            Integer roleId = (Integer) row[0];
            Long count = (Long) row[1];
            result.put(roleId, count);
        }
        return result;
    }
}

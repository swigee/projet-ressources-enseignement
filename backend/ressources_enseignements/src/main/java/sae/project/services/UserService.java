package sae.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sae.project.model.Role;
import sae.project.model.Users;
import sae.project.repositories.RoleRepository;
import sae.project.repositories.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public List<Users> userList() {
        return userRepository.findAll();
    }

    public Optional<Users> getUserById(int id) {
        return userRepository.findById(id);
    }

    public Users saveUser(Users user) {
        return userRepository.save(user);
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    public List<Role> getUserRoles(int id) {
        Optional<Users> user = userRepository.findById(id);
        return user.map(Users::getRoleList).orElse(null);
    }

    public void updateUserRoles(int id, List<String> roles) {
        Optional<Users> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            Users user = userOpt.get();
            List<Role> newRoles = roles.stream()
                .map(roleRepository::findByRights)
                .filter(r -> r != null)
                .toList();
            user.setRoleList(newRoles);
            userRepository.save(user);
        }
    }

    public void removeUserRole(int id, String role) {
        Optional<Users> userOpt = userRepository.findById(id);
        if (userOpt.isPresent() && role != null) {
            Users user = userOpt.get();
            List<Role> roles = user.getRoleList();
            if (roles != null) {

                boolean removed = roles.removeIf(r -> r.getRights() != null && r.getRights().equals(role));
                if (removed) {
                    user.setRoleList(roles);
                    userRepository.save(user);
                    System.out.println("Rôle supprimé et utilisateur sauvegardé.");
                }
            }
        }
    }

    @Transactional
    public void removeUserRoleById(int iduser, int idrole) {
        Users user = userRepository.findById(iduser)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.getRoleList().removeIf(r -> r.getIdrole().equals(idrole));

        userRepository.save(user);
    }
}

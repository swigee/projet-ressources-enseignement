package sae.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sae.project.model.Role;
import sae.project.model.User;
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

    public List<User> userList() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    public List<Role> getUserRoles(int id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(User::getRoleList).orElse(null);
    }

    public void updateUserRoles(int id, List<String> roles) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<Role> newRoles = roles.stream()
                    .map(roleRepository::findByName)
                    .filter(r -> r != null)
                    .toList();
            user.setRoleList(newRoles);
            userRepository.save(user);
        }
    }

    public void removeUserRole(int id, String role) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent() && role != null) {
            User user = userOpt.get();
            List<Role> roles = user.getRoleList();
            if (roles != null) {

                boolean removed = roles.removeIf(r -> r.getName() != null && r.getName().equals(role));
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
        User user = userRepository.findById(iduser)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.getRoleList().removeIf(r -> r.getId().equals(idrole));

        userRepository.save(user);
    }
}

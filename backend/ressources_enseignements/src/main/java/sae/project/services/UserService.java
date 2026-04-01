package sae.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sae.project.model.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import sae.project.model.User;
import sae.project.repositories.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder  passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder  passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> userList() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    public void updateUserRoles(int id, List<Integer> roles) {
        if (roles == null) {
            throw new IllegalArgumentException("La liste des rôles ne doit pas être nulle");
        }
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<Role> newRoles = roles.stream()
                    .map(roleId -> {
                        Optional<Role> roleOpt = roleRepository.findById(roleId);
                        if (roleOpt.isEmpty()) {
                            throw new IllegalArgumentException("Le rôle avec l'id " + roleId + " n'existe pas");
                        }
                        return roleOpt.get();
                    })
                    .collect(Collectors.toCollection(ArrayList::new));
            user.setRoleList(newRoles);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Utilisateur non trouvé pour l'id " + id);
        }
    }

    public void removeAllUserRole(int id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<Role> roles = user.getRoleList();
            if (roles != null && !roles.isEmpty()) {
                roles.clear();
                user.setRoleList(roles);
                userRepository.save(user);
                System.out.println("Tous les rôles supprimés et utilisateur sauvegardé.");
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

    @Transactional
    public void validateUser(int id, String comment) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setValidationStatus("SUBMITTED");
        user.setValidationComment(comment);
        userRepository.save(user);
    }
}

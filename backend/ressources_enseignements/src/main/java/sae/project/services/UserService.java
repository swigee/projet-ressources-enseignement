package sae.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sae.project.dtos.user.BulkImportResultDTO;
import sae.project.model.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import sae.project.model.User;
import sae.project.repositories.*;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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
    public BulkImportResultDTO importUsersFromCsv(MultipartFile file) {
        List<String> errors = new ArrayList<>();
        int successCount = 0;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (lineNumber == 1 && line.toLowerCase().startsWith("username")) continue; // skip header

                String[] parts = line.split(";");
                if (parts.length < 3) {
                    errors.add("Ligne " + lineNumber + " : format invalide (minimum : username;prenom;nom)");
                    continue;
                }

                String username = parts[0].trim();
                String firstName = parts[1].trim();
                String lastName = parts[2].trim();
                String email = parts.length > 3 ? parts[3].trim() : null;
                String rawPassword = parts.length > 4 ? parts[4].trim() : "ChangeMe123";

                if (username.isBlank() || firstName.isBlank() || lastName.isBlank()) {
                    errors.add("Ligne " + lineNumber + " : username, prénom et nom sont obligatoires");
                    continue;
                }

                if (userRepository.findByUsernameIgnoreCase(username).isPresent()) {
                    errors.add("Ligne " + lineNumber + " : l'utilisateur '" + username + "' existe déjà");
                    continue;
                }

                User user = User.builder()
                        .username(username)
                        .firstName(firstName)
                        .lastName(lastName)
                        .email(email)
                        .password(passwordEncoder.encode(rawPassword))
                        .validationStatus("NONE")
                        .build();

                userRepository.save(user);
                successCount++;
            }
        } catch (Exception e) {
            errors.add("Erreur de lecture du fichier : " + e.getMessage());
        }

        return new BulkImportResultDTO(successCount, errors.size(), errors);
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

package sae.project.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sae.project.dtos.user.BulkImportResultDTO;
import sae.project.dtos.user.UserDTO;
import sae.project.model.Role;
import sae.project.model.User;
import sae.project.services.UserService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Users", description = "API for managing users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(path = "/list", produces = { "application/json" })
    @Operation(summary = "Get all users")
    public List<UserDTO> getAllUsers() {
        return userService.userList().stream().map(user -> {
            List<Role> roles = user.getRoleList();
            List<Object> roleJsons = roles != null ? roles.stream().map(role -> (Object) Map.of(
                    "id", role.getId(),
                    "is_active", role.getIsActive(),
                    "name", role.getName()
            )).toList() : List.of();
            List<String> formationJsons = user.getFormationList() != null ? user.getFormationList().stream()
                    .map(f -> f.getName() != null ? f.getName() : String.valueOf(f.getId()))
                    .toList() : List.of();
            List<String> assignmentJsons = user.getAssignmentList() != null ? user.getAssignmentList().stream()
                    .map(a -> String.valueOf(a.getId()))
                    .toList() : List.of();
            return new UserDTO(
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getUsername(),
                    user.getAddress(),
                    user.getEmail(),
                    user.getValidationStatus(),
                    user.getValidationComment(),
                    roleJsons,
                    formationJsons,
                    assignmentJsons
            );
        }).toList();
    }

    @GetMapping(path = "/{id}", produces = { "application/json" })
    @Operation(summary = "Get a user by ID")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        Optional<User> user = userService.getUserById(id);

        return user
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(path = "/{id}/roles", consumes = "application/json")
    @Operation(summary = "Replace all roles of a user")
    public ResponseEntity<Void> updateUserRoles(@PathVariable int id, @RequestBody Map<String, List<Integer>> body) {
        List<Integer> roles = body.get("roles");
        userService.updateUserRoles(id, roles);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{id}/roles", consumes = "application/json")
    @Operation(summary = "Remove a specific role from a user")
    public ResponseEntity<Void> removeUserRole(@PathVariable int id, @RequestBody Map<String, Integer> body) {
        Integer idrole = body.get("idrole");
        userService.removeUserRoleById(id, idrole);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/{id}/validate", produces = { "application/json" })
    @Operation(summary = "Validate a user's service sheet, with an optional comment")
    public ResponseEntity<Void> validateUser(@PathVariable int id, @RequestBody(required = false) Map<String, String> body) {
        String comment = body != null ? body.get("comment") : null;
        userService.validateUser(id, comment);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{id}/allroles", produces = { "application/json" })
    @Operation(summary = "Remove all roles from a user")
    public ResponseEntity<Void> removeAllUserRole(@PathVariable int id) {
        userService.removeAllUserRole(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/import", consumes = "multipart/form-data", produces = "application/json")
    @Operation(summary = "Bulk import users from a CSV file")
    public ResponseEntity<BulkImportResultDTO> importUsers(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        BulkImportResultDTO result = userService.importUsersFromCsv(file);
        return ResponseEntity.ok(result);
    }
}

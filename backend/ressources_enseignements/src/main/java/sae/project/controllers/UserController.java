package sae.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sae.project.dtos.user.UserDTO;
import sae.project.model.Role;
import sae.project.model.User;
import sae.project.services.UserService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(path = "/list", produces = { "application/json" })
    public List<UserDTO> getAllUsers() {
        return userService.userList().stream().map(user -> {
            List<Role> roles = user.getRoleList();
            List<Object> roleJsons = roles != null ? roles.stream().map(role -> (Object) Map.of(
                    "idrole", role.getId(),
                    "title", role.getIsActive(), // mapped title to isActive
                    "rights", role.getName() // mapped rights to name
            )).toList() : List.of();
            return new UserDTO(
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getUsername(),
                    user.getAddress(),
                    user.getEmail(),
                    Boolean.TRUE.equals(user.getIsValidated()),
                    roleJsons,
                    user.getFormationList() != null ? user.getFormationList().stream().map(f -> f.toString()).toList()
                            : List.of(),
                    user.getTicketsList() != null ? user.getTicketsList().stream().map(t -> t.toString()).toList()
                            : List.of(),
                    user.getAssignmentList() != null ? user.getAssignmentList().stream().map(a -> a.toString()).toList()
                            : List.of());
        }).toList();
    }

    @GetMapping(path = "/{id}", produces = { "application/json" })
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        Optional<User> user = userService.getUserById(id);

        return user
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // @PostMapping(path="/add", produces={"application/json"})
    // public User createUser(@RequestBody User user) {
    // return userService.saveUser(user);
    // }

    @DeleteMapping(path = "/{id}", produces = { "application/json" })
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/{id}/roles", consumes = "application/json")
    public ResponseEntity<Void> updateUserRoles(@PathVariable int id, @RequestBody Map<String, List<String>> body) {
        List<String> roles = body.get("roles");
        userService.updateUserRoles(id, roles);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{id}/roles", consumes = "application/json")
    public ResponseEntity<Void> removeUserRole(@PathVariable int id, @RequestBody Map<String, Integer> body) {
        Integer idrole = body.get("idrole"); // Json key expected by FE
        userService.removeUserRoleById(id, idrole);
        return ResponseEntity.ok().build();
    }
}

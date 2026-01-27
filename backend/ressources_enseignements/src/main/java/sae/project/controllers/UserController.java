package sae.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sae.project.model.Users;
import sae.project.services.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping(path="/list", produces={"application/json"})
    public List<Users> getAllUsers() {
        return userService.userList();
    }

    @GetMapping(path="/{id}", produces={"application/json"})
    public ResponseEntity<Users> getUserById(@PathVariable int id) {
        Optional<Users> user = userService.getUserById(id);

        return user
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

//    @PostMapping(path="/add", produces={"application/json"})
//    public Users createUser(@RequestBody Users user) {
//        return userService.saveUser(user);
//    }


    @DeleteMapping(path="/{id}", produces={"application/json"})
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

package sae.project.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sae.project.dtos.auth.LoginRequestDTO;
import sae.project.model.User;
import sae.project.services.AuthService;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Authentication", description = "API for user authentication")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Log in with username and password")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO login) {

        User user = authService.authenticate(
                login.getUsername(),
                login.getPassword());
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(401).body(Map.of("message", "Username or Password incorrect"));
        }

    }
}

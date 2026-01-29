package sae.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sae.project.model.User;
import sae.project.repositories.UserRepository;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public User authenticate(String username, String password) {
        System.out.println("Tentative de connexion pour : " + username);
        Optional<User> usersOptional = userRepository.findByUsername(username);
        if (usersOptional.isPresent()) {
            User user = usersOptional.get();
            System.out.println("usename dans BD : " + user.getUsername());
            System.out.println("mot de passe dans BD : " + user.getPassword());
            System.out.println("Mot de passe reçu" + password);
            if (user.getPassword().equals(password)) {
                System.out.println("--> SUCCÈS : Mots de passe correspondent !");
                return user;
            } else {
                System.out.println("--> ÉCHEC : Mauvais mot de passe.");
            }
        } else {
            System.out.println("--> ÉCHEC : Utilisateur introuvable en base.");
        }
        return null;
    }

}

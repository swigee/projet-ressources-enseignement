package sae.project.dtos;

import java.util.List;

public class UserDTO {
    public int iduser;
    public String firstname;
    public String lastname;
    public String username;
    public String address;
    public String email;
    public boolean servicevalidation;
    public List<Object> roles; // Liste d'objets JSON pour chaque rôle
    public List<String> formations;
    public List<String> tickets;
    public List<String> assignments;

    public UserDTO() {}

    public UserDTO(int iduser, String firstname, String lastname, String username, String address, String email, boolean servicevalidation, List<Object> roles, List<String> formations, List<String> tickets, List<String> assignments) {
        this.iduser = iduser;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.address = address;
        this.email = email;
        this.servicevalidation = servicevalidation;
        this.roles = roles;
        this.formations = formations;
        this.tickets = tickets;
        this.assignments = assignments;
    }
}

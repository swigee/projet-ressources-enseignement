package sae.project.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private int iduser;
    private String firstname;
    private String lastname;
    private String username;
    private String address;
    private String email;
    private boolean servicevalidation;
    private List<Object> roles;
    private List<String> formations;
    private List<String> tickets;
    private List<String> assignments;
}

package sae.project.dtos.teacher;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO pour représenter un enseignant avec ses informations
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class TeacherDTO {
    private Integer id;
    private String name;
    private String firstName;
    private String lastName;
    private String subject; // Matières enseignées
    private String status; // Permanent, Vacataire
    private Integer totalHours; // Heures déjà affectées
    private Integer remainingHours; // Heures restantes
    private String statusColor; // Pour l'interface (bg-green-100, etc.)
    private List<String> roles;
}

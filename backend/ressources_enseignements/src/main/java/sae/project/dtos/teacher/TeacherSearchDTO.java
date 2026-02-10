package sae.project.dtos.teacher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour rechercher des enseignants avec des critères
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherSearchDTO {
    private String keyword; // Mot-clé pour rechercher dans nom/prénom/matière
    private String status; // Filtrer par statut (Permanent, Vacataire)
    private String subject; // Filtrer par matière enseignée
    private Integer minHoursAvailable; // Heures minimales disponibles
}

package sae.project.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * DTO pour les statistiques d'affectation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentStatisticsDTO {
    private Integer totalTeachers; // Nombre total d'enseignants
    private Integer totalAssignments; // Nombre total d'affectations
    private Integer totalHoursAssigned; // Total des heures affectées
    private Integer unassignedModules; // Nombre de modules sans enseignant
    private Map<String, Integer> hoursByLessonType; // Heures par type (CM, TD, TP)
    private Map<String, Integer> hoursByTeacher; // Heures par enseignant
}
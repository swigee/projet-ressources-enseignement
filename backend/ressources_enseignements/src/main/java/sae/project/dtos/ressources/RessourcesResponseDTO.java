package sae.project.dtos.ressources;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO pour la réponse complète de l'API ressources-table
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RessourcesResponseDTO {
    private List<RessourceRowDTO> ressources;
    private List<TeacherBadgeDTO> availableTeachers;
    private List<ScheduleConflictDTO> conflicts;
}

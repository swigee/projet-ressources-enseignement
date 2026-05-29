package sae.project.dtos.ressources;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RessourcesResponseDTO {
    private List<RessourceRowDTO> resources;
    private List<TeacherBadgeDTO> availableTeachers;
    private List<ScheduleConflictDTO> conflicts;
}

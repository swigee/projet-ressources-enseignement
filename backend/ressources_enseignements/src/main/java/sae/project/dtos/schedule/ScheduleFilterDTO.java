package sae.project.dtos.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO pour filtrer les ressources
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleFilterDTO {
    private String year;
    private String className;
    private String formationId;
}

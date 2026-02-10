package sae.project.dtos.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

// DTO pour une ressource dans le planning
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResourceScheduleDTO {
    private Integer id;
    private String courseName;
    private String category;
    private Boolean isHighlighted;
    private Map<String, WeekHoursDTO> hoursPerWeek;
    private Integer hoursPerHalfGroup;
    private Integer totalHours;
    private Integer totalCM;
    private Integer totalTD;
    private Integer totalTP;
}

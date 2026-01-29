package sae.project.dtos;

import lombok.*;
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
    private Map<String, Integer> hoursPerWeek;
    private Integer hoursPerHalfGroup;
    private Integer totalHours;
}

package sae.project.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

// DTO pour le projet SAE
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectScheduleDTO {
    private String id;
    private String name;
    private Map<String, Integer> hoursPerWeek;
    private Integer hoursPerHalfGroup;
    private Integer totalHours;
}

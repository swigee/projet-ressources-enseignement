package sae.project.dtos.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

// DTO pour la mise à jour des heures
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateHoursDTO {
    @com.fasterxml.jackson.annotation.JsonAlias("ressourceId")
    private Integer resourceId;
    private Map<String, WeekHoursDTO> hoursPerWeek;
    private Integer hoursPerHalfGroup;
}

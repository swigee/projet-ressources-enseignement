package sae.project.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

// DTO pour la mise à jour des heures
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateHoursDTO {
    private Integer ressourceId;
    private Map<String, Integer> hoursPerWeek;
    private Integer hoursPerHalfGroup;
}

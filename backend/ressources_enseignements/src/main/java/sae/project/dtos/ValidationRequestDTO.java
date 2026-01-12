package sae.project.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO pour la validation
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationRequestDTO {
    private String selectedYear;
    private String selectedClass;
    private java.util.List<UpdateHoursDTO> ressources;
    private UpdateHoursDTO project;
}

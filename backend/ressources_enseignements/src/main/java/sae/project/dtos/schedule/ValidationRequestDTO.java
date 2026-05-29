package sae.project.dtos.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationRequestDTO {
    private String selectedYear;
    private String selectedClass;
    private String selectedSemester;
    private List<UpdateHoursDTO> resources;
    private UpdateHoursDTO project;
}

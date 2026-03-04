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
public class RessourceRowDTO {
    private Integer id;
    private String moduleName;
    private String category;
    private Integer plannedHours;
    private Integer actualHours;
    private Integer tdHours;
    private Integer tpHours;
    private Integer cmHours;
    private Boolean isHighlighted;
    private List<TeacherBadgeDTO> assignedTeachers;
}

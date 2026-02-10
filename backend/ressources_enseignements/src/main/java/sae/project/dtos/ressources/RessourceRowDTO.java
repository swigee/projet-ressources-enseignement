package sae.project.dtos.ressources;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO pour représenter une ligne de ressource dans le tableau
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RessourceRowDTO {
    private Integer id;
    private String moduleName;
    private String category;
    private Integer heuresPrevisionnelles;
    private Integer heuresReelles;
    private Integer heuresTD;
    private Integer heuresTP;
    private Integer heuresCM;
    private Boolean isHighlighted;
    private List<TeacherBadgeDTO> assignedTeachers;
}

package sae.project.dtos.ressources;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour représenter un enseignant dans un badge
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherBadgeDTO {
    private Integer teacherId;
    private String fullName;  // "MARTIN Louis"
    private Integer assignedHours;
}

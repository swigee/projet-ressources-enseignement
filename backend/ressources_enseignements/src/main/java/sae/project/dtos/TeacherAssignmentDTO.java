package sae.project.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour représenter l'affectation d'un enseignant à un module
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherAssignmentDTO {
    private Integer assignmentId;
    private Integer teacherId;
    private String teacherName;
    private String lessonType; // CM, TD, TP
    private Integer assignedHours; // Nombre d'heures affectées
}
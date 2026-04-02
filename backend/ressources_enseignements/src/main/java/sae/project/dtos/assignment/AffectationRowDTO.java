package sae.project.dtos.assignment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import sae.project.dtos.teacher.TeacherAssignmentDTO;

/**
 * DTO pour représenter une ligne d'affectation dans la grille
 * Correspond à un module/ressource avec ses enseignants affectés
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AffectationRowDTO {
    private Integer resourceId;
    private String module;
    private List<String> groupes; // groupes concernés (null = filtre classe actif)

    // Heures prévues
    private Integer tdHours;
    private Integer tpHours;
    private Integer cmHours;

    // Enseignants affectés par type
    private List<TeacherAssignmentDTO> tdTeachers;
    private List<TeacherAssignmentDTO> tpTeachers;
    private List<TeacherAssignmentDTO> cmTeachers;
}

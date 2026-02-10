package sae.project.dtos.assignment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import sae.project.dtos.teacher.TeacherDTO;

/**
 * DTO pour la grille d'affectation complète
 * Contient tous les éléments nécessaires pour afficher la page d'affectation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentGridDTO {
    private String selectedFormation;
    private String selectedYear;
    private String selectedSemester;
    private List<TeacherDTO> availableTeachers; // Liste des enseignants disponibles
    private List<AffectationRowDTO> affectationGrid; // Grille d'affectation
    private AssignmentStatisticsDTO statistics; // Statistiques
}

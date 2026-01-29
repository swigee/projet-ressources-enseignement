package sae.project.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour filtrer les affectations
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentFilterDTO {
    private String formation; // Formation (ex: "BUT Informatique")
    private String year; // Année d'étude (1, 2, 3)
    private String className; // Classe (ex: "Classe A", "Classe B")
    private String semester; // Semestre (S1, S2)
}
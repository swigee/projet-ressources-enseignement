package sae.project.dtos.assignment;

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
    private String program; // Training program (ex: "BUT Informatique")
    private String year; // Année d'étude (1, 2, 3)
    private String className; // Classe (ex: "Classe A", "Classe B")
    private String semester; // Semestre (S1, S2)
}

package sae.project.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO pour la réponse de validation des affectations
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentValidationResponseDTO {
    private Boolean success; // true si la validation est réussie
    private String message; // Message principal
    private List<String> errors; // Liste des erreurs bloquantes
    private List<String> warnings; // Liste des avertissements non-bloquants
}
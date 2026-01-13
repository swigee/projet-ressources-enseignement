package sae.project.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO pour la réponse de validation
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidationResponseDTO {
    private Boolean success;
    private String message;
    private java.util.List<String> errors;
}

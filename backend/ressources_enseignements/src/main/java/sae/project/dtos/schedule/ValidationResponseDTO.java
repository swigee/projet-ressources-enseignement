package sae.project.dtos.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// DTO pour la réponse de validation
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidationResponseDTO {
    private Boolean success;
    private String message;
    private List<String> errors;
}

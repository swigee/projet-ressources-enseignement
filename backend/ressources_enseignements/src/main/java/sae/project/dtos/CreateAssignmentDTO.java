package sae.project.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO pour créer ou mettre à jour une affectation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAssignmentDTO {

    @NotNull(message = "L'ID de l'enseignant est obligatoire")
    private Integer userId;

    @NotNull(message = "L'ID de la ressource est obligatoire")
    private Integer ressourceId;

    @NotNull(message = "Le type de cours est obligatoire")
    private String lessonType; // CM, TD, TP

    @NotNull(message = "Le nombre d'heures est obligatoire")
    @Positive(message = "Le nombre d'heures doit être positif")
    private Integer assignedTimes;
}
package sae.project.dtos.vacataire;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VacataireDTO {
    private Integer id;

    // Étape 1
    private String responsableRecrutement;
    private String prenom;
    private String nom;
    private String dateNaissance;
    private String departement;
    private String fonction;
    private String experience;
    private String profil;
    private String competences;

    // Étape 2
    private Boolean vueEnAmont;
    private String etablissement;
    private String site;
    private Boolean transmisResponsable;
    private String signatureResponsable;

    // Étape 3
    private String sourceConnaissance;
    private String sourceConnaissanceAutre;

    private String statut;
}

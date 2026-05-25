package sae.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Entity
@Table(name = "vacataire")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vacataire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Link to the user account of this contractor.
     * Null until the candidate has been converted to an active account.
     */
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true)
    @JsonIgnore
    @ToString.Exclude
    private User user;

    // Étape 1 – Fiche de recrutement
    @Column(name = "responsable_recrutement")
    private String responsableRecrutement;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "nom")
    private String nom;

    @Column(name = "date_naissance")
    private String dateNaissance;

    @Column(name = "departement")
    private String departement;

    @Column(name = "fonction")
    private String fonction;

    @Column(name = "experience")
    private String experience;

    @Column(name = "profil")
    private String profil;

    @Column(name = "competences", columnDefinition = "TEXT")
    private String competences;

    // Étape 2 – Tableau de bord
    @Column(name = "vue_en_amont")
    private Boolean vueEnAmont;

    @Column(name = "etablissement")
    private String etablissement;

    @Column(name = "site")
    private String site;

    @Column(name = "transmis_responsable")
    private Boolean transmisResponsable;

    @Column(name = "signature_responsable")
    private String signatureResponsable;

    // Étape 3 – Source de connaissance
    @Column(name = "source_connaissance")
    private String sourceConnaissance;

    @Column(name = "source_connaissance_autre")
    private String sourceConnaissanceAutre;

    /**
     * Recruitment status. Allowed values: A_CONTACTER, EN_COURS, VALIDE.
     * Automatically set to VALIDE when a user account is created.
     */
    @Column(name = "statut")
    @Builder.Default
    private String statut = "A_CONTACTER";
}

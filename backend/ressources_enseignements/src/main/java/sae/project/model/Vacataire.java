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

    /** Link to the user account. Null until the contractor has been converted to an active account. */
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true)
    @JsonIgnore
    @ToString.Exclude
    private User user;

    // Step 1 – Recruitment form
    @Column(name = "responsable_recrutement")
    private String recruitmentManager;

    @Column(name = "prenom")
    private String firstName;

    @Column(name = "nom")
    private String lastName;

    @Column(name = "date_naissance")
    private String birthDate;

    @Column(name = "departement")
    private String department;

    @Column(name = "fonction")
    private String position;

    @Column(name = "experience")
    private String experience;

    @Column(name = "profil")
    private String profile;

    @Column(name = "competences", columnDefinition = "TEXT")
    private String skills;

    // Step 2 – Dashboard
    @Column(name = "vue_en_amont")
    private Boolean advanceNotice;

    @Column(name = "etablissement")
    private String institution;

    @Column(name = "site")
    private String site;

    @Column(name = "transmis_responsable")
    private Boolean sentToManager;

    @Column(name = "signature_responsable")
    private String managerSignature;

    // Step 3 – Knowledge source
    @Column(name = "source_connaissance")
    private String knowledgeSource;

    @Column(name = "source_connaissance_autre")
    private String otherKnowledgeSource;

    /** Recruitment status. Allowed values: A_CONTACTER, EN_COURS, VALIDE. */
    @Column(name = "statut")
    @Builder.Default
    private String status = "A_CONTACTER";
}

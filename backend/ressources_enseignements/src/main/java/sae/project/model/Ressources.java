/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sae.project.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author andry
 */
@Entity
@Table(name = "RESSOURCES")
@NamedQueries({
    @NamedQuery(name = "Ressources.findAll", query = "SELECT r FROM Ressources r"),
    @NamedQuery(name = "Ressources.findByIdressource", query = "SELECT r FROM Ressources r WHERE r.idressource = :idressource"),
    @NamedQuery(name = "Ressources.findByTitle", query = "SELECT r FROM Ressources r WHERE r.title = :title"),
    @NamedQuery(name = "Ressources.findByHeureTdEtat", query = "SELECT r FROM Ressources r WHERE r.heureTdEtat = :heureTdEtat"),
    @NamedQuery(name = "Ressources.findByHeureTdIut", query = "SELECT r FROM Ressources r WHERE r.heureTdIut = :heureTdIut"),
    @NamedQuery(name = "Ressources.findByHeureTpEtat", query = "SELECT r FROM Ressources r WHERE r.heureTpEtat = :heureTpEtat"),
    @NamedQuery(name = "Ressources.findByHeureTpIut", query = "SELECT r FROM Ressources r WHERE r.heureTpIut = :heureTpIut"),
    @NamedQuery(name = "Ressources.findByHeureCmEtat", query = "SELECT r FROM Ressources r WHERE r.heureCmEtat = :heureCmEtat"),
    @NamedQuery(name = "Ressources.findByHeureCmIut", query = "SELECT r FROM Ressources r WHERE r.heureCmIut = :heureCmIut")})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Ressources implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDRESSOURCE")
    private Integer idressource;

    @Column(name = "TITLE", nullable = false, length = 255)
    private String title;

    @Lob
    @Column(name = "DESCRIPTION", columnDefinition = "TEXT")
    private String description;

    @Column(name = "CATEGORY", length = 100)
    private String category;

    @Column(name = "IS_HIGHLIGHTED")
    private Boolean isHighlighted = false;

    // Heures TD (Travaux Dirigés)
    @Column(name = "HEURE_TD_ETAT")
    private Integer heureTdEtat;

    @Column(name = "HEURE_TD_IUT")
    private Integer heureTdIut;

    // Heures TP (Travaux Pratiques)
    @Column(name = "HEURE_TP_ETAT")
    private Integer heureTpEtat;

    @Column(name = "HEURE_TP_IUT")
    private Integer heureTpIut;

    // Heures CM (Cours Magistral)
    @Column(name = "HEURE_CM_ETAT")
    private Integer heureCmEtat;

    @Column(name = "HEURE_CM_IUT")
    private Integer heureCmIut;

    // Stockage des heures par semaine (JSON)
    @Column(name = "HOURS_PER_WEEK", columnDefinition = "TEXT")
    private String hoursPerWeekJson;

    // Heures par demi-groupe
    @Column(name = "HOURS_PER_HALF_GROUP")
    private Integer hoursPerHalfGroup;

    // Relations
    @ManyToMany(mappedBy = "ressourcesList", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("ressourcesList")
    private List<Formation> formationList;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "RESSOURCESSYLLABUS",
            joinColumns = @JoinColumn(name = "IDRESSOURCE"),
            inverseJoinColumns = @JoinColumn(name = "IDSYLLABUS")
    )
    @JsonIgnoreProperties("ressourcesList")
    private List<Syllabus> syllabusList;

    @OneToMany(mappedBy = "idressource", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("idressource")
    private List<Assignment> assignmentList;

    // Méthodes utilitaires pour gérer hoursPerWeek comme Map
    @Transient
    public Map<String, Integer> getHoursPerWeek() {
        if (hoursPerWeekJson == null || hoursPerWeekJson.isEmpty()) {
            return new HashMap<>();
        }
        try {
            // Conversion JSON vers Map (nécessite Jackson ObjectMapper)
            com.fasterxml.jackson.databind.ObjectMapper mapper =
                    new com.fasterxml.jackson.databind.ObjectMapper();
            return mapper.readValue(hoursPerWeekJson,
                    new com.fasterxml.jackson.core.type.TypeReference<Map<String, Integer>>() {
                    });
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    @Transient
    public void setHoursPerWeek(Map<String, Integer> hoursPerWeek) {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper =
                    new com.fasterxml.jackson.databind.ObjectMapper();
            this.hoursPerWeekJson = mapper.writeValueAsString(hoursPerWeek);
        } catch (Exception e) {
            this.hoursPerWeekJson = "{}";
        }
    }

    // Calcul du total des heures
    @Transient
    public Integer getTotalHours() {
        return getHoursPerWeek().values().stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ressources)) return false;
        Ressources that = (Ressources) o;
        return idressource != null && idressource.equals(that.idressource);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Ressources{" +
                "idressource=" + idressource +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
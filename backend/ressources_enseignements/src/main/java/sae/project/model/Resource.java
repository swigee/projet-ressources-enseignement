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
@Table(name = "resource")
@NamedQueries({
        @NamedQuery(name = "Resource.findAll", query = "SELECT r FROM Resource r"),
        @NamedQuery(name = "Resource.findById", query = "SELECT r FROM Resource r WHERE r.id = :id"),
        @NamedQuery(name = "Resource.findByTitle", query = "SELECT r FROM Resource r WHERE r.title = :title"),
        @NamedQuery(name = "Resource.findByTdStateHours", query = "SELECT r FROM Resource r WHERE r.tdStateHours = :tdStateHours"),
        @NamedQuery(name = "Resource.findByTdIutHours", query = "SELECT r FROM Resource r WHERE r.tdIutHours = :tdIutHours"),
        @NamedQuery(name = "Resource.findByTpStateHours", query = "SELECT r FROM Resource r WHERE r.tpStateHours = :tpStateHours"),
        @NamedQuery(name = "Resource.findByTpIutHours", query = "SELECT r FROM Resource r WHERE r.tpIutHours = :tpIutHours"),
        @NamedQuery(name = "Resource.findByCmStateHours", query = "SELECT r FROM Resource r WHERE r.cmStateHours = :cmStateHours"),
        @NamedQuery(name = "Resource.findByCmIutHours", query = "SELECT r FROM Resource r WHERE r.cmIutHours = :cmIutHours") })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Resource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    private String title;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "category", length = 100)
    private String category;

    @Column(name = "is_highlighted")
    @Builder.Default
    private Boolean isHighlighted = false;

    // Heures TD (Travaux Dirigés)
    @Column(name = "td_state_hours")
    private Integer tdStateHours;

    @Column(name = "td_iut_hours")
    private Integer tdIutHours;

    // Heures TP (Travaux Pratiques)
    @Column(name = "tp_state_hours")
    private Integer tpStateHours;

    @Column(name = "tp_iut_hours")
    private Integer tpIutHours;

    // Heures CM (Cours Magistral)
    @Column(name = "cm_state_hours")
    private Integer cmStateHours;

    @Column(name = "cm_iut_hours")
    private Integer cmIutHours;

    // Stockage des heures par semaine (JSON)
    @Column(name = "hours_per_week", columnDefinition = "TEXT")
    private String hoursPerWeekJson;

    // Heures par demi-groupe
    @Column(name = "hours_per_half_group")
    private Integer hoursPerHalfGroup;

    // Relations
    @ManyToMany(mappedBy = "resourceList", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("resourceList")
    private List<Formation> formationList;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "resource_syllabus", joinColumns = @JoinColumn(name = "resource_id"), inverseJoinColumns = @JoinColumn(name = "syllabus_id"))
    @JsonIgnoreProperties("resourceList")
    private List<Syllabus> syllabusList;

    @OneToMany(mappedBy = "resource", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("resource")
    private List<Assignment> assignmentList;

    // Méthodes utilitaires pour gérer hoursPerWeek comme Map
    @Transient
    public Map<String, Integer> getHoursPerWeek() {
        if (hoursPerWeekJson == null || hoursPerWeekJson.isEmpty()) {
            return new HashMap<>();
        }
        try {
            // Conversion JSON vers Map (nécessite Jackson ObjectMapper)
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
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
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
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
        if (this == o)
            return true;
        if (!(o instanceof Resource))
            return false;
        Resource that = (Resource) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Resource{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                '}';
    }

}

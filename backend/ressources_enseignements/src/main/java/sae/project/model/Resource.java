package sae.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import sae.project.dtos.schedule.WeekHoursDTO;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "category", length = 100)
    private String category;

    @Column(name = "savoirs", columnDefinition = "TEXT")
    private String savoirs;

    @Column(name = "apprentissages_critiques", columnDefinition = "TEXT")
    private String apprentissagesCritiques;

    @Column(name = "volume_officiel", columnDefinition = "TEXT")
    private String volumeOfficiel;

    @Column(name = "personal_description", columnDefinition = "TEXT")
    private String personalDescription;

    @Column(name = "personal_savoirs", columnDefinition = "TEXT")
    private String personalSavoirs;

    @Column(name = "personal_apprentissages", columnDefinition = "TEXT")
    private String personalApprentissages;

    @Column(name = "personal_volume", columnDefinition = "TEXT")
    private String personalVolume;

    @Column(name = "td_state_hours")
    private Integer tdStateHours;

    @Column(name = "td_iut_hours")
    private Integer tdIutHours;

    @Column(name = "tp_state_hours")
    private Integer tpStateHours;

    @Column(name = "tp_iut_hours")
    private Integer tpIutHours;

    @Column(name = "cm_state_hours")
    private Integer cmStateHours;

    @Column(name = "cm_iut_hours")
    private Integer cmIutHours;

    @Column(name = "hours_per_week", columnDefinition = "TEXT")
    private String hoursPerWeekJson;

    @Column(name = "hours_per_half_group")
    private Integer hoursPerHalfGroup;

    @Column(name = "semester")
    private Integer semester;

    
    //TODO plus tard (revoir si ces deux proprietes servent a quelque chose)
    @ManyToMany(mappedBy = "resourceList", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Formation> formationList;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "resource_syllabus", joinColumns = @JoinColumn(name = "resource_id"), inverseJoinColumns = @JoinColumn(name = "syllabus_id"))
    @JsonIgnoreProperties("resourceList")
    private List<Syllabus> syllabusList;

    @OneToMany(mappedBy = "resource", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("resource")
    private List<Assignment> assignmentList;

    @Transient
    public Map<String, WeekHoursDTO> getHoursPerWeek() {
        if (hoursPerWeekJson == null || hoursPerWeekJson.isEmpty()) {
            return new HashMap<>();
        }
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        try {
            return mapper.readValue(hoursPerWeekJson,
                    new com.fasterxml.jackson.core.type.TypeReference<Map<String, WeekHoursDTO>>() {
                    });
        } catch (Exception e) {
            try {
                Map<String, Integer> oldFormat = mapper.readValue(hoursPerWeekJson,
                        new com.fasterxml.jackson.core.type.TypeReference<Map<String, Integer>>() {
                        });
                Map<String, WeekHoursDTO> converted = new HashMap<>();
                for (Map.Entry<String, Integer> entry : oldFormat.entrySet()) {
                    converted.put(entry.getKey(),
                            WeekHoursDTO.builder()
                                    .cm(0).td(0).tp(0)
                                    .total(entry.getValue() != null ? entry.getValue() : 0)
                                    .build());
                }
                return converted;
            } catch (Exception ex) {
                return new HashMap<>();
            }
        }
    }

    @Transient
    public void setHoursPerWeek(Map<String, WeekHoursDTO> hoursPerWeek) {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            this.hoursPerWeekJson = mapper.writeValueAsString(hoursPerWeek);
        } catch (Exception e) {
            this.hoursPerWeekJson = "{}";
        }
    }

    @Transient
    public Integer getTotalHours() {
        return getHoursPerWeek().values().stream()
                .mapToInt(w -> (w.getCm() != null ? w.getCm() : 0)
                             + (w.getTd() != null ? w.getTd() : 0)
                             + (w.getTp() != null ? w.getTp() : 0))
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

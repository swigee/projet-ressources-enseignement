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
@Table(name = "semester")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Semester implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "semester_id")
    private Integer semester_id;
    
    @Column(name="year")
    private Integer year;
    
    @Column(name="semester_number")
    private Integer semester_number;
    
    @Column(name="parcours")
    private String parcours;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "semester_resource", joinColumns = @JoinColumn(name = "semester_id"), inverseJoinColumns = @JoinColumn(name = "resource_id"))
    @JsonIgnoreProperties({"semesterList", "formationList"})
    private List<Resource> resourceList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formation_id")
    @JsonIgnoreProperties("semesters")
    private Formation formation;
}


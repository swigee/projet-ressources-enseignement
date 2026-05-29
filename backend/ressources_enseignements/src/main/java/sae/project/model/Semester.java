package sae.project.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

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

    @Column(name = "year")
    private Integer year;

    @Column(name = "semester_number")
    private Integer semester_number;

    @Column(name = "parcours")
    private String pathway;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "semester_resource", joinColumns = @JoinColumn(name = "semester_id"), inverseJoinColumns = @JoinColumn(name = "resource_id"))
    @JsonIgnoreProperties({"semesterList", "programs"})
    private List<Resource> resources;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formation_id")
    @JsonIgnoreProperties("semesters")
    private Formation program;
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sae.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

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
public class Ressources implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IDRESSOURCE")
    private Integer idressource;
    @Column(name = "TITLE")
    private String title;
    @Lob
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "HEURE_TD_ETAT")
    private String heureTdEtat;
    @Column(name = "HEURE_TD_IUT")
    private String heureTdIut;
    @Column(name = "HEURE_TP_ETAT")
    private String heureTpEtat;
    @Column(name = "HEURE_TP_IUT")
    private String heureTpIut;
    @Column(name = "HEURE_CM_ETAT")
    private String heureCmEtat;
    @Column(name = "HEURE_CM_IUT")
    private String heureCmIut;
    @JsonIgnore
    @ManyToMany(mappedBy = "ressourcesList")
    private List<Formation> formationList;
    @JsonIgnore
    @JoinTable(name = "RESSOURCESSYLLABUS", joinColumns = {
        @JoinColumn(name = "IDRESSOURCE", referencedColumnName = "IDRESSOURCE")}, inverseJoinColumns = {
        @JoinColumn(name = "IDSYLLABUS", referencedColumnName = "IDSYLLABUS")})
    @ManyToMany
    private List<Syllabus> syllabusList;
    @OneToMany(mappedBy = "idressource")
    private List<Assignment> assignmentList;

    public Ressources() {
    }

    public Ressources(Integer idressource) {
        this.idressource = idressource;
    }

    public Integer getIdressource() {
        return idressource;
    }

    public void setIdressource(Integer idressource) {
        this.idressource = idressource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHeureTdEtat() {
        return heureTdEtat;
    }

    public void setHeureTdEtat(String heureTdEtat) {
        this.heureTdEtat = heureTdEtat;
    }

    public String getHeureTdIut() {
        return heureTdIut;
    }

    public void setHeureTdIut(String heureTdIut) {
        this.heureTdIut = heureTdIut;
    }

    public String getHeureTpEtat() {
        return heureTpEtat;
    }

    public void setHeureTpEtat(String heureTpEtat) {
        this.heureTpEtat = heureTpEtat;
    }

    public String getHeureTpIut() {
        return heureTpIut;
    }

    public void setHeureTpIut(String heureTpIut) {
        this.heureTpIut = heureTpIut;
    }

    public String getHeureCmEtat() {
        return heureCmEtat;
    }

    public void setHeureCmEtat(String heureCmEtat) {
        this.heureCmEtat = heureCmEtat;
    }

    public String getHeureCmIut() {
        return heureCmIut;
    }

    public void setHeureCmIut(String heureCmIut) {
        this.heureCmIut = heureCmIut;
    }

    public List<Formation> getFormationList() {
        return formationList;
    }

    public void setFormationList(List<Formation> formationList) {
        this.formationList = formationList;
    }

    public List<Syllabus> getSyllabusList() {
        return syllabusList;
    }

    public void setSyllabusList(List<Syllabus> syllabusList) {
        this.syllabusList = syllabusList;
    }

    public List<Assignment> getAssignmentList() {
        return assignmentList;
    }

    public void setAssignmentList(List<Assignment> assignmentList) {
        this.assignmentList = assignmentList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idressource != null ? idressource.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ressources)) {
            return false;
        }
        Ressources other = (Ressources) object;
        if ((this.idressource == null && other.idressource != null) || (this.idressource != null && !this.idressource.equals(other.idressource))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sae.project.model.Ressources[ idressource=" + idressource + " ]";
    }
    
}

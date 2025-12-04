/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sae.project.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author andry
 */
@Entity
@Table(name = "SYLLABUS")
@NamedQueries({
    @NamedQuery(name = "Syllabus.findAll", query = "SELECT s FROM Syllabus s"),
    @NamedQuery(name = "Syllabus.findByIdsyllabus", query = "SELECT s FROM Syllabus s WHERE s.idsyllabus = :idsyllabus"),
    @NamedQuery(name = "Syllabus.findByDescriptions", query = "SELECT s FROM Syllabus s WHERE s.descriptions = :descriptions")})
public class Syllabus implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDSYLLABUS")
    private Integer idsyllabus;
    @Column(name = "DESCRIPTIONS")
    private String descriptions;
    @ManyToMany(mappedBy = "syllabusList")
    private List<Ressources> ressourcesList;

    public Syllabus() {
    }

    public Syllabus(Integer idsyllabus) {
        this.idsyllabus = idsyllabus;
    }

    public Integer getIdsyllabus() {
        return idsyllabus;
    }

    public void setIdsyllabus(Integer idsyllabus) {
        this.idsyllabus = idsyllabus;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public List<Ressources> getRessourcesList() {
        return ressourcesList;
    }

    public void setRessourcesList(List<Ressources> ressourcesList) {
        this.ressourcesList = ressourcesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idsyllabus != null ? idsyllabus.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Syllabus)) {
            return false;
        }
        Syllabus other = (Syllabus) object;
        if ((this.idsyllabus == null && other.idsyllabus != null) || (this.idsyllabus != null && !this.idsyllabus.equals(other.idsyllabus))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sae.project.model.Syllabus[ idsyllabus=" + idsyllabus + " ]";
    }
    
}

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
@Table(name = "syllabus")
@NamedQueries({
        @NamedQuery(name = "Syllabus.findAll", query = "SELECT s FROM Syllabus s"),
        @NamedQuery(name = "Syllabus.findById", query = "SELECT s FROM Syllabus s WHERE s.id = :id"),
        @NamedQuery(name = "Syllabus.findByDescription", query = "SELECT s FROM Syllabus s WHERE s.description = :description") })
public class Syllabus implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "description")
    private String description;
    @JsonIgnore
    @ManyToMany(mappedBy = "syllabusList")
    private List<Resource> resourceList;

    public Syllabus() {
    }

    public Syllabus(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Resource> getResourceList() {
        return resourceList;
    }

    public void setResourceList(List<Resource> resourceList) {
        this.resourceList = resourceList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Syllabus)) {
            return false;
        }
        Syllabus other = (Syllabus) object;
        if ((this.id == null && other.id != null)
                || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sae.project.model.Syllabus[ id=" + id + " ]";
    }

}

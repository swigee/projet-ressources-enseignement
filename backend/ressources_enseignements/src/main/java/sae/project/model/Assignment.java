/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sae.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import java.io.Serializable;

/**
 *
 * @author andry
 */
@Entity
@Table(name = "ASSIGNMENT")
@NamedQueries({
    @NamedQuery(name = "Assignment.findAll", query = "SELECT a FROM Assignment a"),
    @NamedQuery(name = "Assignment.findByIdAssignment", query = "SELECT a FROM Assignment a WHERE a.idAssignment = :idAssignment"),
    @NamedQuery(name = "Assignment.findByAssignedtimes", query = "SELECT a FROM Assignment a WHERE a.assignedtimes = :assignedtimes"),
    @NamedQuery(name = "Assignment.findByLessontype", query = "SELECT a FROM Assignment a WHERE a.lessontype = :lessontype")})
public class Assignment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_ASSIGNMENT")
    private Integer idAssignment;
    @Column(name = "ASSIGNEDTIMES")
    private Integer assignedtimes;
    @Column(name = "LESSONTYPE")
    private String lessontype;
    @JsonIgnore
    @JoinColumn(name = "IDRESSOURCE", referencedColumnName = "IDRESSOURCE")
    @ManyToOne
    private Ressources idressource;
    @JsonIgnore
    @JoinColumn(name = "IDUSER", referencedColumnName = "IDUSER")
    @ManyToOne
    private Users iduser;

    public Assignment() {
    }

    public Assignment(Integer idAssignment) {
        this.idAssignment = idAssignment;
    }

    public Integer getIdAssignment() {
        return idAssignment;
    }

    public void setIdAssignment(Integer idAssignment) {
        this.idAssignment = idAssignment;
    }

    public Integer getAssignedtimes() {
        return assignedtimes;
    }

    public void setAssignedtimes(Integer assignedtimes) {
        this.assignedtimes = assignedtimes;
    }

    public String getLessontype() {
        return lessontype;
    }

    public void setLessontype(String lessontype) {
        this.lessontype = lessontype;
    }

    public Ressources getIdressource() {
        return idressource;
    }

    public void setIdressource(Ressources idressource) {
        this.idressource = idressource;
    }

    public Users getIduser() {
        return iduser;
    }

    public void setIduser(Users iduser) {
        this.iduser = iduser;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAssignment != null ? idAssignment.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Assignment)) {
            return false;
        }
        Assignment other = (Assignment) object;
        if ((this.idAssignment == null && other.idAssignment != null) || (this.idAssignment != null && !this.idAssignment.equals(other.idAssignment))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sae.project.model.Assignment[ idAssignment=" + idAssignment + " ]";
    }
    
}

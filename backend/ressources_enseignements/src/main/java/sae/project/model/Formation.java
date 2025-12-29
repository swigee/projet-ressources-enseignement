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
@Table(name = "FORMATION")
@NamedQueries({
    @NamedQuery(name = "Formation.findAll", query = "SELECT f FROM Formation f"),
    @NamedQuery(name = "Formation.findByIdformation", query = "SELECT f FROM Formation f WHERE f.idformation = :idformation"),
    @NamedQuery(name = "Formation.findByName", query = "SELECT f FROM Formation f WHERE f.name = :name")})
public class Formation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IDFORMATION")
    private Integer idformation;
    @Column(name = "NAME")
    private String name;
    @JoinTable(name = "USERFORMATION", joinColumns = {
        @JoinColumn(name = "IDFORMATION", referencedColumnName = "IDFORMATION")}, inverseJoinColumns = {
        @JoinColumn(name = "IDUSER", referencedColumnName = "IDUSER")})
    @JsonIgnore
    @ManyToMany
    private List<Users> usersList;
    @JoinTable(name = "FORMATIONRESSOURCES", joinColumns = {
        @JoinColumn(name = "IDFORMATION", referencedColumnName = "IDFORMATION")}, inverseJoinColumns = {
        @JoinColumn(name = "IDRESSOURCE", referencedColumnName = "IDRESSOURCE")})
    @JsonIgnore
    @ManyToMany
    private List<Ressources> ressourcesList;

    public Formation() {
    }

    public Formation(Integer idformation) {
        this.idformation = idformation;
    }

    public Integer getIdformation() {
        return idformation;
    }

    public void setIdformation(Integer idformation) {
        this.idformation = idformation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Users> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<Users> usersList) {
        this.usersList = usersList;
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
        hash += (idformation != null ? idformation.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Formation)) {
            return false;
        }
        Formation other = (Formation) object;
        if ((this.idformation == null && other.idformation != null) || (this.idformation != null && !this.idformation.equals(other.idformation))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sae.project.model.Formation[ idformation=" + idformation + " ]";
    }
    
}

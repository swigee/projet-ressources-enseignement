/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sae.project.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
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
@Table(name = "ROLE")
@NamedQueries({
    @NamedQuery(name = "Role.findAll", query = "SELECT r FROM Role r"),
    @NamedQuery(name = "Role.findByIdrole", query = "SELECT r FROM Role r WHERE r.idrole = :idrole"),
    @NamedQuery(name = "Role.findByTitle", query = "SELECT r FROM Role r WHERE r.title = :title"),
    @NamedQuery(name = "Role.findByRights", query = "SELECT r FROM Role r WHERE r.rights = :rights")})
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDROLE")
    private Integer idrole;
    @Column(name = "TITLE")
    private Boolean title;
    @Column(name = "RIGHTS")
    private String rights;
    @JoinTable(name = "USERROLE", joinColumns = {
        @JoinColumn(name = "IDROLE", referencedColumnName = "IDROLE")}, inverseJoinColumns = {
        @JoinColumn(name = "IDUSER", referencedColumnName = "IDUSER")})
    @ManyToMany
    private List<Users> usersList;

    public Role() {
    }

    public Role(Integer idrole) {
        this.idrole = idrole;
    }

    public Integer getIdrole() {
        return idrole;
    }

    public void setIdrole(Integer idrole) {
        this.idrole = idrole;
    }

    public Boolean getTitle() {
        return title;
    }

    public void setTitle(Boolean title) {
        this.title = title;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    public List<Users> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<Users> usersList) {
        this.usersList = usersList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idrole != null ? idrole.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Role)) {
            return false;
        }
        Role other = (Role) object;
        if ((this.idrole == null && other.idrole != null) || (this.idrole != null && !this.idrole.equals(other.idrole))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sae.project.model.Role[ idrole=" + idrole + " ]";
    }
    
}

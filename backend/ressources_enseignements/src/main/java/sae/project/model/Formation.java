/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sae.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Entité Formation représentant une formation avec année et classe
 * 
 * @author andry
 */
@Entity
@Table(name = "formation")
@NamedQueries({
        @NamedQuery(name = "Formation.findAll", query = "SELECT f FROM Formation f"),
        @NamedQuery(name = "Formation.findById", query = "SELECT f FROM Formation f WHERE f.id = :id"),
        @NamedQuery(name = "Formation.findByName", query = "SELECT f FROM Formation f WHERE f.name = :name"),
        @NamedQuery(name = "Formation.findByYearAndClassName", query = "SELECT f FROM Formation f WHERE f.year = :year AND f.className = :className")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Formation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "year", length = 10)
    private String year;

    @Column(name = "class_name", length = 50)
    private String className;

    @ManyToMany
    @JoinTable(name = "user_formation", joinColumns = @JoinColumn(name = "formation_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonIgnore
    private List<User> usersList; // Keeping usersList naming for now to minimize getter refactoring, but type is
                                  // User

    @ManyToMany
    @JoinTable(name = "formation_resource", joinColumns = @JoinColumn(name = "formation_id"), inverseJoinColumns = @JoinColumn(name = "resource_id"))
    @JsonIgnore
    private List<Resource> resourceList;

    /**
     * Constructeur avec ID uniquement
     */
    public Formation(Integer id) {
        this.id = id;
    }
    
}

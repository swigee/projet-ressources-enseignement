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
 *
 * @author andry
 */
@Entity
@Table(name = "FORMATION")
@NamedQueries({
        @NamedQuery(name = "Formation.findAll", query = "SELECT f FROM Formation f"),
        @NamedQuery(name = "Formation.findByIdformation", query = "SELECT f FROM Formation f WHERE f.idformation = :idformation"),
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
    @Column(name = "IDFORMATION")
    private Integer idformation;

    @Column(name = "NAME", length = 255)
    private String name;

    @Column(name = "YEAR", length = 10)
    private String year;

    @Column(name = "CLASS_NAME", length = 50)
    private String className;

    @ManyToMany
    @JoinTable(
            name = "USERFORMATION",
            joinColumns = @JoinColumn(name = "IDFORMATION"),
            inverseJoinColumns = @JoinColumn(name = "IDUSER")
    )
    @JsonIgnore
    private List<Users> usersList;

    @ManyToMany
    @JoinTable(
            name = "FORMATIONRESSOURCES",
            joinColumns = @JoinColumn(name = "IDFORMATION"),
            inverseJoinColumns = @JoinColumn(name = "IDRESSOURCE")
    )
    @JsonIgnore
    private List<Ressources> ressourcesList;

    /**
     * Constructeur avec ID uniquement
     */
    public Formation(Integer idformation) {
        this.idformation = idformation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Formation)) return false;
        Formation formation = (Formation) o;
        return idformation != null && idformation.equals(formation.idformation);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Formation{" +
                "idformation=" + idformation +
                ", name='" + name + '\'' +
                ", year='" + year + '\'' +
                ", className='" + className + '\'' +
                '}';
    }
}
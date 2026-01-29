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
@Table(name = "assignment")
@NamedQueries({
        @NamedQuery(name = "Assignment.findAll", query = "SELECT a FROM Assignment a"),
        @NamedQuery(name = "Assignment.findById", query = "SELECT a FROM Assignment a WHERE a.id = :id"),
        @NamedQuery(name = "Assignment.findByAssignedTimes", query = "SELECT a FROM Assignment a WHERE a.assignedTimes = :assignedTimes"),
        @NamedQuery(name = "Assignment.findByLessonType", query = "SELECT a FROM Assignment a WHERE a.lessonType = :lessonType") })
public class Assignment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "assigned_times")
    private Integer assignedTimes;

    @Column(name = "lesson_type")
    private String lessonType;

    @JsonIgnore
    @JoinColumn(name = "resource_id", referencedColumnName = "id")
    @ManyToOne
    private Resource resource;

    @JsonIgnore
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private User user;

    public Assignment() {
    }

    public Assignment(Integer id) {
        this.id = id;
    }

    // Getters and Setters manually updated for now (or could switch to Lombok)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAssignedTimes() {
        return assignedTimes;
    }

    public void setAssignedTimes(Integer assignedTimes) {
        this.assignedTimes = assignedTimes;
    }

    public String getLessonType() {
        return lessonType;
    }

    public void setLessonType(String lessonType) {
        this.lessonType = lessonType;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

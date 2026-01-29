/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sae.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author andry
 */
@Entity
@Table(name = "ticket") // singular
@NamedQueries({
        @NamedQuery(name = "Ticket.findAll", query = "SELECT t FROM Ticket t"),
        @NamedQuery(name = "Ticket.findById", query = "SELECT t FROM Ticket t WHERE t.id = :id"),
        @NamedQuery(name = "Ticket.findByTitle", query = "SELECT t FROM Ticket t WHERE t.title = :title"),
        @NamedQuery(name = "Ticket.findByDescription", query = "SELECT t FROM Ticket t WHERE t.description = :description"),
        @NamedQuery(name = "Ticket.findByDate", query = "SELECT t FROM Ticket t WHERE t.date = :date"),
        @NamedQuery(name = "Ticket.findByStatus", query = "SELECT t FROM Ticket t WHERE t.status = :status") })
public class Ticket implements Serializable { // Renamed class too, will rename file next

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(name = "status")
    private String status;

    @JsonIgnore
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private User user;

    public Ticket() {
    }

    public Ticket(Integer id) {
        this.id = id;
    }

    // Getters/Setters manually
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

package sae.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

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
    @ManyToMany(mappedBy = "syllabuses")
    private List<Resource> resources;

    public Syllabus() {}

    public Syllabus(Integer id) {
        this.id = id;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<Resource> getResources() { return resources; }
    public void setResources(List<Resource> resources) { this.resources = resources; }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Syllabus)) return false;
        Syllabus other = (Syllabus) object;
        return (this.id == null && other.id == null)
                || (this.id != null && this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "Syllabus[id=" + id + "]";
    }
}

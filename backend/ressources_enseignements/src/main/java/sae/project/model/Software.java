package sae.project.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "software")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Software implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "version")
    private String version;

    @Column(name = "plugins", columnDefinition = "TEXT")
    private String plugins;

    @Column(name = "license")
    private String license;

    @Column(name = "period")
    private String period;

    @Column(name = "status")
    private String status; // "INSTALLED" or "REQUESTED"

    @Column(name = "year")
    private String year;

    @Column(name = "resource_names", columnDefinition = "TEXT")
    private String resourceNames;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"password", "assignments", "ticketsList", "programs", "roles"})
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id")
    @JsonIgnoreProperties({"programs", "syllabusList", "assignments", "hoursPerWeekJson"})
    private Resource resource;
}

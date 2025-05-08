package dev.zrdzn.hiresynapse.hiresynapsebackend.model;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import dev.zrdzn.hiresynapse.hiresynapsebackend.shared.stat.StatPoint;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "candidates")
public class Candidate implements StatPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false)
    private Instant createdAt;

    @Column
    private Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @Column(nullable = false)
    private String email;

    private CandidateStatus status;
    private TaskStatus taskStatus;
    private String utmSource;

    private String firstName;
    private String lastName;
    private String phone;
    private String executiveSummary;
    private String analysedSummary;
    private String careerTrajectory;
    private Integer yearsOfRelatedExperience;
    private Integer yearsOfExperience;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, String> relatedExperience;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, String> experience;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, String> education;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, String> skills;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, String> projects;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private List<String> languages;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private List<String> certificates;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private List<String> employerReferences;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private List<String> keyAchievements;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private List<String> keySoftSkills;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

}

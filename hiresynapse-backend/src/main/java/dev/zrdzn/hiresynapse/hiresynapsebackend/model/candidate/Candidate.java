package dev.zrdzn.hiresynapse.hiresynapsebackend.model.candidate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.TaskStatus;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.interview.Interview;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.job.Job;
import dev.zrdzn.hiresynapse.hiresynapsebackend.shared.statistic.StatisticPoint;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class Candidate implements StatisticPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false)
    private Instant createdAt;

    @Column
    private Instant updatedAt;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @Column(nullable = false)
    private String email;

    @Column
    @Enumerated(EnumType.STRING)
    private CandidateStatus status;

    @Column
    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;

    @Column
    private String utmSource;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String phone;

    @Column(length = 400)
    private String executiveSummary;

    @Column(length = 400)
    private String analysedSummary;

    @Column(length = 400)
    private String careerTrajectory;

    @Column
    private Integer yearsOfRelatedExperience;

    @Column
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

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<Interview> interviews;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public Candidate(Job job, String email, CandidateStatus status, TaskStatus taskStatus, String utmSource) {
        this.job = job;
        this.email = email;
        this.status = status;
        this.taskStatus = taskStatus;
        this.utmSource = utmSource;
    }

}

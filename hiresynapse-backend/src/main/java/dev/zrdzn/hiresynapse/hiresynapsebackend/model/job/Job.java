package dev.zrdzn.hiresynapse.hiresynapsebackend.model.job;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.TaskStatus;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.candidate.Candidate;
import dev.zrdzn.hiresynapse.hiresynapsebackend.shared.statistic.StatisticPoint;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "jobs")
public class Job implements StatisticPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false)
    private Instant createdAt;

    @Column
    private Instant updatedAt;

    @Column
    private Instant publishAt;

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 50, message = "Title must be between 3 and 50 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 3, max = 1000, message = "Description must be between 3 and 1000 characters")
    private String description;

    @NotBlank(message = "Location is required")
    private String location;

    @Column
    private String salary;

    @Column
    private String requiredExperience;

    @Column
    @Enumerated(EnumType.STRING)
    private JobStatus status;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private List<String> requirements;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private List<String> benefits;

    @Column
    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;

    @OneToMany(mappedBy = "job", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<Candidate> candidates;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public Job(
        String title,
        String description,
        String location,
        String salary,
        String requiredExperience,
        JobStatus status,
        List<String> requirements,
        List<String> benefits,
        TaskStatus taskStatus
    ) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.salary = salary;
        this.requiredExperience = requiredExperience;
        this.status = status;
        this.requirements = requirements;
        this.benefits = benefits;
        this.taskStatus = taskStatus;
    }

}
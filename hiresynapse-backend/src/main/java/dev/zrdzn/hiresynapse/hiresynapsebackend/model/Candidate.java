package dev.zrdzn.hiresynapse.hiresynapsebackend.model;

import dev.zrdzn.hiresynapse.hiresynapsebackend.shared.stat.StatPoint;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "candidates")
public class Candidate implements StatPoint, Serializable {

    @Id
    private String id;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    @DBRef
    private Job job;

    @NotBlank(message = "Email is required")
    private String email;

    private CandidateStatus status;
    private TaskStatus taskStatus;

    private String firstName;
    private String lastName;
    private String phone;
    private String executiveSummary;
    private String analysedSummary;
    private String careerTrajectory;
    private Integer yearsOfRelatedExperience;
    private Integer yearsOfExperience;
    private Map<String, String> relatedExperience;
    private Map<String, String> experience;
    private Map<String, String> education;
    private Map<String, String> skills;
    private Map<String, String> projects;
    private List<String> languages;
    private List<String> certificates;
    private List<String> references;
    private List<String> keyAchievements;
    private List<String> keySoftSkills;

}

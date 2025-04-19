package dev.zrdzn.hiresynapse.hiresynapsebackend.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "candidates")
public class Candidate implements Serializable {

    @Id
    @Nullable
    private String id;

    private String firstName;
    private String lastName;

    @NotBlank(message = "Email is required")
    private String email;

    private String phone;

    @DBRef
    private Job job;

    private Integer matchScore;
    private CandidateStatus status;
    private Map<String, Integer> experience;
    private Map<String, String> education;
    private Map<String, String> skills;
    private List<String> languages;
    private List<String> certificates;
    private List<String> references;
    private TaskStatus taskStatus;

}

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

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    private String email;

    private String phone;

    @DBRef
    private Job job;

    private int matchScore;
    private String status;
    private Map<String, Integer> experience;
    private Map<String, String> education;
    private Map<String, String> skills;
    private List<String> languages;
    private List<String> certificates;
    private List<String> references;
    private double salaryExpectation;
    private TaskStatus taskStatus;

}

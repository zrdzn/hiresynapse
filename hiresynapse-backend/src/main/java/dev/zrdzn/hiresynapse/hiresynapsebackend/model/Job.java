package dev.zrdzn.hiresynapse.hiresynapsebackend.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "jobs")
public class Job implements Serializable {

    @Id
    private String id;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 50, message = "Title must be between 3 and 50 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 3, max = 1000, message = "Description must be between 3 and 1000 characters")
    private String description;

    @NotBlank(message = "Location is required")
    private String location;

    private String salary;

    private String requiredExperience;

    private JobStatus status;

    private List<String> requirements;

    private List<String> benefits;

    private TaskStatus taskStatus;

}

package dev.zrdzn.hiresynapse.hiresynapsebackend.model;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.task.TaskEntity;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.task.TaskEntityType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "jobs")
public class Job implements TaskEntity, Serializable {

    @Id
    @Nullable
    private String id;

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 50, message = "Title must be between 3 and 50 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 3, max = 1000, message = "Description must be between 3 and 1000 characters")
    private String description;

    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Location is required")
    private String location;

    private String salary;

    private String requiredExperience;

    @NotBlank(message = "Status is required")
    private String status;

    private String requirements;

    @Override
    public TaskEntityType getType() {
        return TaskEntityType.JOB;
    }

}

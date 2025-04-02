package dev.zrdzn.hiresynapse.hiresynapsebackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "jobs")
public class Job {

    @Id
    @Nullable
    private String id;

    private String jobTitle;
    private String description;
    private String companyName;
    private String location;
    private double salary;
    private int requiredExperience;
    private String status;
    private ProcessStatus processStatus;
    private List<String> requirements;

}

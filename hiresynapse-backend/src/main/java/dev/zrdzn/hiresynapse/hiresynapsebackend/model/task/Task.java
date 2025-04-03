package dev.zrdzn.hiresynapse.hiresynapsebackend.model.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tasks")
public class Task {

    @Id
    private String id;

    private TaskStatus status;

    private TaskEntity entity;

    @Nullable
    private String initiatorId;

    @Nullable
    private String initiatorEmail;

}

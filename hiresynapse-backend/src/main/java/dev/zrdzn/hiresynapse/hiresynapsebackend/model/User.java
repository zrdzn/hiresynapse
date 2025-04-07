package dev.zrdzn.hiresynapse.hiresynapsebackend.model;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.task.TaskEntity;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.task.TaskEntityType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User implements TaskEntity, Serializable {

    @Id
    private String id;

    private String username;
    private String email;
    private String role;
    private String pictureUrl;

    @Override
    public TaskEntityType getType() {
        return TaskEntityType.USER;
    }

}

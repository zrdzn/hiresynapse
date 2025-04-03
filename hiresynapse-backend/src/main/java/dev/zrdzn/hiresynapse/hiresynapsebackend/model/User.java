package dev.zrdzn.hiresynapse.hiresynapsebackend.model;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.task.TaskEntity;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.task.TaskEntityType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
public class User implements TaskEntity {

    @Id
    private String id;

    private String username;
    private String password;
    private String email;
    private String role;

    @Override
    public TaskEntityType getType() {
        return TaskEntityType.USER;
    }

}

package dev.zrdzn.hiresynapse.hiresynapsebackend.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class User implements Serializable {

    @Id
    private String id;

    private String username;

    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Role is required")
    private UserRole role;

    private String pictureUrl;

}

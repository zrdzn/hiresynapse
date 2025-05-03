package dev.zrdzn.hiresynapse.hiresynapsebackend.model;

import dev.zrdzn.hiresynapse.hiresynapsebackend.shared.stat.StatPoint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;

@Data
@Document(collection = "users")
public class User implements StatPoint, Serializable {

    @Id
    private String id;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    @NotBlank(message = "Username is required")
    private String username;

    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Role is required")
    private UserRole role;

    private String pictureUrl;

    private User() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final User user;

        public Builder() {
            this.user = new User();
        }

        public Builder id(String id) {
            this.user.id = id;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.user.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(Instant updatedAt) {
            this.user.updatedAt = updatedAt;
            return this;
        }

        public Builder username(String username) {
            this.user.username = username;
            return this;
        }

        public Builder email(String email) {
            this.user.email = email;
            return this;
        }

        public Builder firstName(String firstName) {
            this.user.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.user.lastName = lastName;
            return this;
        }

        public Builder role(UserRole role) {
            this.user.role = role;
            return this;
        }

        public Builder pictureUrl(String pictureUrl) {
            this.user.pictureUrl = pictureUrl;
            return this;
        }

        public User build() {
            return this.user;
        }

    }

}

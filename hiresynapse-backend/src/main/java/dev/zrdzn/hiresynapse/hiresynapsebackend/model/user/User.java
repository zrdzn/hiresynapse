package dev.zrdzn.hiresynapse.hiresynapsebackend.model.user;

import dev.zrdzn.hiresynapse.hiresynapsebackend.shared.statistic.StatisticPoint;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements StatisticPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false)
    private Instant createdAt;

    @Column
    private Instant updatedAt;

    @NotBlank(message = "Username is required")
    @Column(nullable = false, unique = true)
    private String username;

    @Email(message = "Email should be valid")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "First name is required")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Column(nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    private String pictureUrl;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final User user;

        public Builder() {
            this.user = new User();
        }

        public Builder id(Long id) {
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

package dev.zrdzn.hiresynapse.hiresynapsebackend.repository;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);

}

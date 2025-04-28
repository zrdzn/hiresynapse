package dev.zrdzn.hiresynapse.hiresynapsebackend.service;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.User;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.UserRole;
import dev.zrdzn.hiresynapse.hiresynapsebackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(String id, String username, String email, String firstName, String lastName, UserRole role, String pictureUrl) {
        User user = userRepository.save(new User(id, username, firstName, lastName, email, role, pictureUrl));
        return new User(user.getId(), user.getUsername(), user.getEmail(), firstName, lastName, user.getRole(), user.getPictureUrl());
    }

    public Optional<User> getUser(String id) {
        return userRepository.findById(id)
            .map(user -> new User(user.getId(), user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getRole(), user.getPictureUrl()));
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .map(user -> new User(user.getId(), user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getRole(), user.getPictureUrl()));
    }

}

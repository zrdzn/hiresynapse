package dev.zrdzn.hiresynapse.hiresynapsebackend.service;

import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.UserDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.User;
import dev.zrdzn.hiresynapse.hiresynapsebackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto createUser(String id, String username, String email, String role, String pictureUrl) {
        User user = userRepository.save(new User(id, username, email, role, pictureUrl));
        return new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole(), user.getPictureUrl());
    }

    public Optional<UserDto> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .map(user -> new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole(), user.getPictureUrl()));
    }

}

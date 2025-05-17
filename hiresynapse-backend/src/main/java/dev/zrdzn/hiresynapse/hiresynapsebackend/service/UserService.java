package dev.zrdzn.hiresynapse.hiresynapsebackend.service;

import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.statistic.MonthlyDataDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.error.ApiError;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.user.User;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.user.UserRole;
import dev.zrdzn.hiresynapse.hiresynapsebackend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static dev.zrdzn.hiresynapse.hiresynapsebackend.shared.statistic.StatisticHelper.getMonthlyData;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(String username, String email, String firstName, String lastName, UserRole role, String pictureUrl) {
        User user = User.builder()
            .username(username)
            .email(email)
            .firstName(firstName)
            .lastName(lastName)
            .role(role)
            .pictureUrl(pictureUrl)
            .build();

        try {
            User createdUser = userRepository.save(user);

            logger.debug("Created user: {}", createdUser.getId());

            return createdUser;
        } catch (Exception exception) {
            logger.error("Failed to save user entity", exception);
            throw new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save user entity");
        }
    }

    public List<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable).getContent();
    }

    public Optional<User> getUser(long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public int getUserCount() {
        return (int) userRepository.count();
    }

    public MonthlyDataDto getUsersFromLastSixMonths() {
        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);
        Instant startDate = sixMonthsAgo.atStartOfDay(ZoneId.systemDefault()).toInstant();

        List<User> users = userRepository.findUsersCreatedAfter(startDate);

        return getMonthlyData(users);
    }

    public void deleteUser(long requesterId, long userId) {
        if (requesterId == userId) {
            throw new ApiError(HttpStatus.FORBIDDEN, "You cannot delete your own account");
        }

        userRepository.deleteById(userId);

        logger.debug("User with id {} deleted", userId);
    }

}

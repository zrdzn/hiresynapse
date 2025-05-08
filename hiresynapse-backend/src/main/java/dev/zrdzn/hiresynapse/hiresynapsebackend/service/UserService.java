package dev.zrdzn.hiresynapse.hiresynapsebackend.service;

import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.MonthlyDataDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.error.ApiError;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.User;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.UserRole;
import dev.zrdzn.hiresynapse.hiresynapsebackend.repository.UserRepository;
import dev.zrdzn.hiresynapse.hiresynapsebackend.shared.stat.StatHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
            return userRepository.save(user);
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

        Map<String, Integer> monthlyData = StatHelper.countByMonth(users);
        double growthRate = StatHelper.calculateGrowthRate(monthlyData);

        return new MonthlyDataDto(
            users.size(),
            growthRate,
            monthlyData
        );
    }

    public void deleteUser(long requesterId, long id) {
        if (requesterId == id) {
            throw new ApiError(HttpStatus.FORBIDDEN, "You cannot delete your own account");
        }

        userRepository.deleteById(id);
        logger.debug("User with id {} deleted", id);
    }

}

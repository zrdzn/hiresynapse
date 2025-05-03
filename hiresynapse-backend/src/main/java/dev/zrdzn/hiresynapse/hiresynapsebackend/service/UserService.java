package dev.zrdzn.hiresynapse.hiresynapsebackend.service;

import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.MonthlyDataDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.User;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.UserRole;
import dev.zrdzn.hiresynapse.hiresynapsebackend.repository.UserRepository;
import dev.zrdzn.hiresynapse.hiresynapsebackend.shared.stat.StatHelper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;

    public UserService(UserRepository userRepository, MongoTemplate mongoTemplate) {
        this.userRepository = userRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public User createUser(String id, String username, String email, String firstName, String lastName, UserRole role, String pictureUrl) {
        User user = User.builder()
            .id(id)
            .username(username)
            .email(email)
            .firstName(firstName)
            .lastName(lastName)
            .role(role)
            .pictureUrl(pictureUrl)
            .build();

        return userRepository.save(user);
    }

    public Optional<User> getUser(String id) {
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

        Query query = new Query();
        query.addCriteria(Criteria.where("createdAt").gte(startDate));

        List<User> users = mongoTemplate.find(query, User.class);

        Map<String, Integer> monthlyData = StatHelper.countByMonth(users);
        double growthRate = StatHelper.calculateGrowthRate(monthlyData);

        return new MonthlyDataDto(
            users.size(),
            growthRate,
            monthlyData
        );
    }

}

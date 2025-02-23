package dev.oleksii.rotamanagementapp.repos;

import dev.oleksii.rotamanagementapp.domain.entities.User;
import dev.oleksii.rotamanagementapp.domain.entities.VerificationToken;
import dev.oleksii.rotamanagementapp.domain.enums.Role;
import dev.oleksii.rotamanagementapp.domain.repos.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    private User user;
    private String token;

    @BeforeEach
    void setUp() {
        token = UUID.randomUUID().toString();
        user = User.builder()
                .fullName("Test")
                .password("password")
                .email("test@email.com")
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .verified(false)
                .verificationToken(new VerificationToken(token, LocalDateTime.now()))
                .build();
    }

    @Test
    void testFindByVerificationToken() {
        userRepository.save(user);
        Optional<User> userOptional = userRepository.findByVerificationToken(token);

        assert userOptional.isPresent();
        assert userOptional.get().getId().equals(user.getId());
        assert userOptional.get().getEmail().equals(user.getEmail());
    }

    @Test
    void testFindByEmail() {
        userRepository.save(user);
        Optional<User> userOptional = userRepository.findByEmail(user.getEmail());
        assert userOptional.isPresent();
        assert userOptional.get().getId().equals(user.getId());
        assert userOptional.get().getEmail().equals("test@email.com");
    }

    @Test
    void testExistsByEmail() {
        userRepository.save(user);
        boolean exists = userRepository.existsByEmail(user.getEmail());
        assert exists;
    }
}

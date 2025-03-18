package dev.oleksii.rotamanagementapp.reposIT;

import dev.oleksii.rotamanagementapp.domain.entities.User;
import dev.oleksii.rotamanagementapp.domain.entities.VerificationToken;
import dev.oleksii.rotamanagementapp.domain.enums.Role;
import dev.oleksii.rotamanagementapp.domain.repos.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryIT {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TestEntityManager entityManager;

    private User user;
    private String token;

    @BeforeEach
    void setUp() {
        token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(token, LocalDateTime.now());

        user = User.builder()
                .fullName("Test")
                .password("password")
                .email("test@email.com")
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .verified(false)
                .verificationToken(verificationToken)
                .build();
    }

    @Test
    void testFindByVerificationToken() {
        user = entityManager.persistAndFlush(user);

        Optional<User> userOptional = userRepository.findByVerificationToken(token);

        assertThat(userOptional).isPresent();
        assertThat(userOptional.get().getId()).isEqualTo(user.getId());
        assertThat(userOptional.get().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void testFindByEmail() {
        user = entityManager.persistAndFlush(user);

        Optional<User> userOptional = userRepository.findByEmail(user.getEmail());

        assertThat(userOptional).isPresent();
        assertThat(userOptional.get().getId()).isEqualTo(user.getId());
        assertThat(userOptional.get().getEmail()).isEqualTo("test@email.com");
    }

    @Test
    void testExistsByEmail() {
        user = entityManager.persistAndFlush(user);

        boolean exists = userRepository.existsByEmail(user.getEmail());

        assertThat(exists).isTrue();
    }
}
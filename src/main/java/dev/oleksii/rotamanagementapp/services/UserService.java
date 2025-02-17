package dev.oleksii.rotamanagementapp.services;

import dev.oleksii.rotamanagementapp.domain.dtos.UserDto;
import dev.oleksii.rotamanagementapp.domain.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Provides service methods for user management, extending the Spring Security
 * {@link UserDetailsService} for authentication and user details loading.
 */
public interface UserService extends UserDetailsService {

    /**
     * Creates a new user in the system based on the provided {@link UserDto}.
     *
     * @param request the data transfer object containing user details.
     * @return the newly created {@link User} entity.
     */
    User createUser(UserDto request);

    /**
     * Updates the given {@link User} entity with the details provided in the
     * {@link UserDto}.
     *
     * @param user the existing user entity to be updated.
     * @param request the DTO containing updated user details.
     */
    void updateUserDetails(User user, UserDto request);

    /**
     * Deletes the specified {@link User} from the system.
     *
     * @param user the user entity to delete.
     */
    void deleteUser(User user);
}

package tripway.server.service.users;

import tripway.server.models.User;

import java.util.List;

/**
 * @author Nikita Burtelov
 */
public interface UserService {
    User getUserId(Long id);

    List<User> getAllUser();

    void saveUser(User user);

    void removeUser(User user);

    void removeUser(Long id);

    void updateUser(User user);

    void updateUser(Long id);
}

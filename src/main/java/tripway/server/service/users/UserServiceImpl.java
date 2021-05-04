package tripway.server.service.users;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tripway.server.models.User;
import tripway.server.repository.UserRepository;

import java.util.List;

/**
 * @author Nikita Burtelov
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User getUserId(Long id) { return userRepository.findById(id).orElse(null); }

    @Override
    public List<User> getAllUser() {return userRepository.findAll();}

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void removeUser(User user) {
        userRepository.delete(user);
    }

    @Override
    public void removeUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public void updateUser(Long id) {

    }

    public List<User> getAll() {
        return userRepository.findAll();
    }
}

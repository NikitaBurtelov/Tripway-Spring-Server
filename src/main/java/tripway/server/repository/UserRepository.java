package tripway.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tripway.server.models.User;

/**
 * @author Nikita Burtelov
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}

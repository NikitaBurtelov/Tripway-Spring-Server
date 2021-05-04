package tripway.server.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import tripway.server.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
}

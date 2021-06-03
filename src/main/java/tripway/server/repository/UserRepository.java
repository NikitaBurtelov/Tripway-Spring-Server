package tripway.server.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import tripway.server.models.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String> {
}

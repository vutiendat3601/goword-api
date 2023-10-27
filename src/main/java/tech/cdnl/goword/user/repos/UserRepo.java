package tech.cdnl.goword.user.repos;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.cdnl.goword.user.models.entity.User;

public interface UserRepo extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByVerificationCode(String verifCode);
}

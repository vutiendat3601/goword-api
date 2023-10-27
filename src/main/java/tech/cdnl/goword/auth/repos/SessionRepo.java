package tech.cdnl.goword.auth.repos;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.cdnl.goword.auth.models.entity.Session;

public interface SessionRepo extends JpaRepository<Session, UUID> {
    Optional<Session> findByRefreshToken(String refreshToken);
}

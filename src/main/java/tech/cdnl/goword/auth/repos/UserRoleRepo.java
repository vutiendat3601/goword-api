package tech.cdnl.goword.auth.repos;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tech.cdnl.goword.auth.models.entity.UserRole;
import java.util.List;

public interface UserRoleRepo extends JpaRepository<UserRole, UUID> {

    @Query(value = " SELECT * FROM user_role WHERE active = true AND user_id = :userId ", nativeQuery = true)
    List<UserRole> findAllByUserId(@Param("userId") UUID userId);
}

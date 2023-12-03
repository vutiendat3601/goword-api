package tech.cdnl.goword.auth.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tech.cdnl.goword.auth.models.entity.UserRole;
import java.util.List;
import java.util.UUID;

public interface UserRoleRepo extends JpaRepository<UserRole, Long> {

    @Query(value = " SELECT * FROM user_role WHERE active = true AND user_id = :userId ", nativeQuery = true)
    List<UserRole> findAllByUserId(@Param("userId") UUID userId);
}

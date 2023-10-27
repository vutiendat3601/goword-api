package tech.cdnl.goword.auth.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tech.cdnl.goword.auth.models.entity.RolePermission;

public interface RolePermissionRepo extends JpaRepository<RolePermission, Long> {
    @Query(value = " SELECT * FROM role_permission WHERE active = true AND role_name IN :roleNames ", nativeQuery = true)
    List<RolePermission> findAllByRoleNames(@Param("roleNames") List<String> roleNames);
}

package tech.cdnl.goword.plan.repos;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.cdnl.goword.plan.models.entity.Plan;

public interface PlanRepo extends JpaRepository<Plan, UUID> {

    boolean existsByName(String name);
}

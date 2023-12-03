package tech.cdnl.goword.exercise.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tech.cdnl.goword.exercise.models.entity.ExercisePlan;

public interface ExercisePlanRepo extends JpaRepository<ExercisePlan, Long> {

    @Query(" SELECT ep FROM ExercisePlan ep WHERE active = true AND planName = :planName")
    public List<ExercisePlan> findAllByPlanName(@Param("planName") String planName);

}

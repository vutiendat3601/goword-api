package tech.cdnl.goword.exercise.repos;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tech.cdnl.goword.exercise.models.entity.Exercise;

public interface ExerciseRepo extends JpaRepository<Exercise, UUID> {

    @Query(" SELECT e FROM Exercise e WHERE e.id IN :exerciseIds")
    List<Exercise> findAllByExerciseIds(@Param("exerciseIds") List<UUID> exerciseIds);
}

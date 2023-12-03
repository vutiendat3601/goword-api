package tech.cdnl.goword.exercise.models.mapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import tech.cdnl.goword.exercise.models.dto.ExerciseDto;
import tech.cdnl.goword.exercise.models.entity.Exercise;

@Component
public class ExerciseMapper {
    @Value("${app.static_resource_url}")
    private String staticResourceUrl;

    public ExerciseDto mapToDto(Exercise exercise) {
        return new ExerciseDto(
                exercise.getId(),
                exercise.getName(),
                staticResourceUrl + exercise.getThumb(),
                exercise.getContent(),
                exercise.getCreatedAt());
    }
}

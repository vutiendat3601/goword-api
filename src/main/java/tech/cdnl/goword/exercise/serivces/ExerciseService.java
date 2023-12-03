package tech.cdnl.goword.exercise.serivces;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import tech.cdnl.goword.exercise.models.dto.ExerciseDto;

public interface ExerciseService {
    void createExercise(String name, String description, MultipartFile thumb);

    Page<ExerciseDto> getExercises(int page, int size);

    void deleteExercise(UUID exId);

    ExerciseDto getExercise(UUID exId);
}

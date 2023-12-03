package tech.cdnl.goword.exercise.serivces.impl;

import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import tech.cdnl.goword.exceptions.AppErrorMessage;
import tech.cdnl.goword.exceptions.ResourceNotFoundException;
import tech.cdnl.goword.exercise.models.dto.ExerciseDto;
import tech.cdnl.goword.exercise.models.entity.Exercise;
import tech.cdnl.goword.exercise.models.mapper.ExerciseMapper;
import tech.cdnl.goword.exercise.repos.ExerciseRepo;
import tech.cdnl.goword.exercise.serivces.ExerciseService;
import tech.cdnl.goword.exercise.serivces.ExerciseServiceAsync;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExerciseServiceImpl implements ExerciseService {
    private final ExerciseMapper exMapper;

    private final ExerciseRepo exRepo;
    private final ExerciseServiceAsync exServiceAsync;

    @Override
    public void createExercise(String name, String content, MultipartFile thumb) {
        String fileName = RandomString.make() + "." + FilenameUtils.getExtension(thumb.getOriginalFilename());
        String filePath = "/images/" + fileName;
        if (thumb != null) {
            log.info("[ %s ] Save thumb image with file path: %s".formatted(getClass().getSimpleName(), filePath));
            exServiceAsync.saveThumb(filePath, thumb);
        }
        Exercise exercise = new Exercise(name, content, filePath);
        exRepo.save(exercise);
    }

    @Override
    public Page<ExerciseDto> getExercises(int page, int size) {
        Page<Exercise> exsPage = exRepo.findAll(PageRequest.of(page, size));
        Page<ExerciseDto> exDtosPage = exsPage.map(exMapper::mapToDto);
        return exDtosPage;
    }

    @Override
    public void deleteExercise(UUID exId) {
        Exercise ex = exRepo.findById(exId)
                .orElseThrow(() -> new ResourceNotFoundException(AppErrorMessage.EXERCISE_NOT_FOUND));
        ex.setDeleted(true);
        exRepo.save(ex);
    }

    @Override
    public ExerciseDto getExercise(UUID exId) {
        Exercise ex = exRepo.findById(exId)
                .orElseThrow(() -> new ResourceNotFoundException(AppErrorMessage.EXERCISE_NOT_FOUND));
        ExerciseDto exDto = exMapper.mapToDto(ex);
        return exDto;
    }

}

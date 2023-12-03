package tech.cdnl.goword.exercise.controllers;

import java.util.UUID;

import javax.validation.constraints.NotBlank;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import tech.cdnl.goword.exercise.models.dto.ExerciseDto;
import tech.cdnl.goword.exercise.serivces.ExerciseService;
import tech.cdnl.goword.shared.models.ApiResponseStatus;
import tech.cdnl.goword.shared.models.PageResponse;
import tech.cdnl.goword.shared.models.Response;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/exercises")
public class ExerciseController {
    private final ExerciseService exService;

    @PreAuthorize("hasAuthority('CREATE_EXERCISE')")
    @PostMapping
    public ResponseEntity<Response<?>> createPlan(
            @RequestParam(required = false) MultipartFile thumb,
            @NotBlank @RequestParam String name,
            @NotBlank @RequestParam String content) {
        exService.createExercise(name, content, thumb);
        return ResponseEntity.ok(new Response<>(ApiResponseStatus.SUCCESS));
    }

    @GetMapping
    public ResponseEntity<PageResponse<ExerciseDto>> getExercises(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ExerciseDto> planDtosPage = exService.getExercises(page, size);
        return ResponseEntity.ok(
                new PageResponse<>(
                        ApiResponseStatus.SUCCESS,
                        planDtosPage.getContent(),
                        planDtosPage.getNumber(),
                        planDtosPage.getTotalPages(),
                        planDtosPage.getSize(),
                        planDtosPage.getNumberOfElements()));
    }

    @GetMapping("{id}")
    public ResponseEntity<Response<ExerciseDto>> getExercise(@PathVariable("id") UUID exId) {
        ExerciseDto exDto = exService.getExercise(exId);
        return ResponseEntity.ok(new Response<>(ApiResponseStatus.SUCCESS, exDto));
    }

    @PreAuthorize("hasAuthority('DELETE_EXERCISE')")
    @DeleteMapping("{id}")
    public ResponseEntity<Response<Boolean>> deletePlan(@PathVariable("id") UUID exId) {
        exService.deleteExercise(exId);
        return ResponseEntity.ok(
                new Response<>(ApiResponseStatus.SUCCESS, true));
    }
}

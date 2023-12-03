package tech.cdnl.goword.plan.services.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import tech.cdnl.goword.exceptions.AppErrorCode;
import tech.cdnl.goword.exceptions.AppErrorMessage;
import tech.cdnl.goword.exceptions.ResourceConflictException;
import tech.cdnl.goword.exceptions.ResourceNotFoundException;
import tech.cdnl.goword.exercise.models.dto.ExerciseDto;
import tech.cdnl.goword.exercise.models.entity.Exercise;
import tech.cdnl.goword.exercise.models.entity.ExercisePlan;
import tech.cdnl.goword.exercise.models.mapper.ExerciseMapper;
import tech.cdnl.goword.exercise.repos.ExercisePlanRepo;
import tech.cdnl.goword.exercise.repos.ExerciseRepo;
import tech.cdnl.goword.plan.models.dto.PlanDto;
import tech.cdnl.goword.plan.models.entity.Plan;
import tech.cdnl.goword.plan.models.mapper.PlanMapper;
import tech.cdnl.goword.plan.repos.PlanRepo;
import tech.cdnl.goword.plan.services.PlanService;
import tech.cdnl.goword.plan.services.PlanServiceAsync;

@Slf4j
@RequiredArgsConstructor
@Service
public class PlanServiceImpl implements PlanService {
    private final PlanMapper planMapper;
    private final ExerciseMapper exMapper;

    @Value("${app.static_resource_url}")
    private String staticResourceUrl;

    private final PlanServiceAsync planServiceAsync;

    private final PlanRepo planRepo;

    private final ExercisePlanRepo exPlanRepo;

    private final ExerciseRepo exRepo;

    @Override
    public void createPlan(String name, String description, MultipartFile thumb) {
        if (planRepo.existsByName(name)) {
            throw new ResourceConflictException(
                    AppErrorMessage.PLAN_NAME_DUPLICATED,
                    AppErrorCode.RESOURCE_CONFLICT_PLAN_DUPLICATED);
        }
        String fileName = RandomString.make() + "." + FilenameUtils.getExtension(thumb.getOriginalFilename());
        String filePath = "/images/" + fileName;
        if (thumb != null) {
            log.info("[ %s ] Save thumb image with file path: %s".formatted(getClass().getSimpleName(), filePath));
            planServiceAsync.saveThumb(filePath, thumb);
        }
        Plan plan = new Plan(name, description, filePath);
        planRepo.save(plan);
    }

    @Override
    public Page<PlanDto> getPlans(int page, int size) {
        Page<Plan> plansPage = planRepo.findAll(PageRequest.of(page, size));
        Page<PlanDto> planDtosPage = plansPage.map(planMapper::mapToDto);
        return planDtosPage;
    }

    @Override
    public void deletePlan(UUID planId) {
        Plan plan = planRepo.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException(AppErrorMessage.PLAN_NOT_FOUND));
        plan.setDeleted(true);
        planRepo.save(plan);
    }

    @Override
    public PlanDto getPlan(UUID planId) {
        Plan plan = planRepo.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException(AppErrorMessage.PLAN_NOT_FOUND));
        PlanDto planDto = planMapper.mapToDto(plan);
        List<ExercisePlan> exPlans = exPlanRepo.findAllByPlanName(plan.getName());
        List<Exercise> exs = exRepo
                .findAllByExerciseIds(exPlans.stream().map(exPlan -> exPlan.getExerciseId()).toList());
        List<ExerciseDto> exDtos = exs.stream().map(exMapper::mapToDto).toList();
        planDto.setExercises(exDtos);
        return planDto;
    }

    @Override
    public void addExercises(UUID planId, List<UUID> exerciseIds) {
        final Plan plan = planRepo.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException(AppErrorMessage.PLAN_NOT_FOUND));
        exerciseIds.forEach(exId -> {
            if (exRepo.existsById(exId)) {
                ExercisePlan exPlan = new ExercisePlan(exId, plan.getName(), true);
                exPlanRepo.save(exPlan);
            }
        });
    }

    @Override
    public void removeExercises(UUID planId, List<UUID> exerciseIds) {
        final Plan plan = planRepo.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException(AppErrorMessage.PLAN_NOT_FOUND));
        List<ExercisePlan> exPlans = exPlanRepo.findAllByPlanName(plan.getName());
        Set<UUID> unqExerciseIds = new HashSet<>(exerciseIds);
        exPlans.forEach(exPlan -> {
            if (unqExerciseIds.contains(exPlan.getExerciseId())) {
                exPlan.setActive(false);
                exPlanRepo.save(exPlan);
            }
        });
    }

}

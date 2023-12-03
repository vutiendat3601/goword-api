package tech.cdnl.goword.plan.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import tech.cdnl.goword.plan.models.dto.PlanDto;

public interface PlanService {
    void createPlan(String name, String description, MultipartFile thumb);

    Page<PlanDto> getPlans(int page, int size);

    void deletePlan(UUID planId);

    PlanDto getPlan(UUID planId);

    void addExercises(UUID planId, List<UUID> exerciseIds);

    void removeExercises(UUID planId, List<UUID> exerciseIds);
}

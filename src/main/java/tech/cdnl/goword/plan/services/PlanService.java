package tech.cdnl.goword.plan.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import tech.cdnl.goword.plan.models.dto.PlanDto;

public interface PlanService {
    void createPlan(String name, String description, MultipartFile avatar);

    Page<PlanDto> getPlan(int page, int size);

    void deletePlan(UUID planId);
}

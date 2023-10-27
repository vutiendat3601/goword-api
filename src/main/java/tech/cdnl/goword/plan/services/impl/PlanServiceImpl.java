package tech.cdnl.goword.plan.services.impl;

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
import tech.cdnl.goword.plan.models.dto.PlanDto;
import tech.cdnl.goword.plan.models.entity.Plan;
import tech.cdnl.goword.plan.repos.PlanRepo;
import tech.cdnl.goword.plan.services.PlanService;
import tech.cdnl.goword.plan.services.PlanServiceAsync;

@Slf4j
@RequiredArgsConstructor
@Service
public class PlanServiceImpl implements PlanService {
    @Value("${app.static_resource_url}")
    private String staticResourceUrl;

    private final PlanServiceAsync planServiceAsync;

    private final PlanRepo planRepo;

    @Override
    public void createPlan(String name, String description, MultipartFile avatar) {
        if (planRepo.existsByName(name)) {
            throw new ResourceConflictException(
                    AppErrorMessage.PLAN_NAME_DUPLICATED,
                    AppErrorCode.RESOURCE_CONFLICT_PLAN_DUPLICATED);
        }
        String filePath = null;
        String fileName = RandomString.make() + "." + FilenameUtils.getExtension(avatar.getOriginalFilename());
        filePath = "/images/" + fileName;
        if (avatar != null) {
            log.info("[ %s ] Save avatar image with file path: %s".formatted(getClass().getSimpleName(), filePath));
            planServiceAsync.saveAvatar(filePath, avatar);
        }
        Plan plan = new Plan(name, description, filePath);
        planRepo.save(plan);
    }

    @Override
    public Page<PlanDto> getPlan(int page, int size) {
        Page<Plan> plansPage = planRepo.findAll(PageRequest.of(page, size));
        Page<PlanDto> planDtosPage = plansPage.map(plan -> new PlanDto(
                plan.getId(),
                plan.getName(),
                plan.getDescription(),
                staticResourceUrl + plan.getAvatar()));
        return planDtosPage;
    }

    @Override
    public void deletePlan(UUID planId) {
        Plan plan = planRepo.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException(AppErrorMessage.PLAN_NOT_FOUND));
        plan.setDeleted(true);
        planRepo.save(plan);
    }

}

package tech.cdnl.goword.plan.controllers;

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
import tech.cdnl.goword.plan.models.dto.PlanDto;
import tech.cdnl.goword.plan.services.PlanService;
import tech.cdnl.goword.shared.models.ApiResponseStatus;
import tech.cdnl.goword.shared.models.PageResponse;
import tech.cdnl.goword.shared.models.Response;

@RequiredArgsConstructor
@RequestMapping("api/v1/plans")
@RestController
public class PlanController {

    private final PlanService planService;

    @Validated
    @PreAuthorize("hasAuthority('CREATE_PLAN')")
    @PostMapping
    public ResponseEntity<Response<?>> createPlan(
            @RequestParam(required = false) MultipartFile avatar,
            @NotBlank @RequestParam String name,
            @RequestParam(required = false) String description) {
        planService.createPlan(name, description, avatar);
        return ResponseEntity.ok(new Response<>(ApiResponseStatus.SUCCESS));
    }

    @Validated
    @PreAuthorize("hasAuthority('VIEW_ALL_PLAN')")
    @GetMapping
    public ResponseEntity<PageResponse<PlanDto>> getPlan(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<PlanDto> planDtosPage = planService.getPlan(page, size);
        return ResponseEntity.ok(
                new PageResponse<>(
                        ApiResponseStatus.SUCCESS,
                        planDtosPage.getContent(),
                        planDtosPage.getNumber(),
                        planDtosPage.getTotalPages(),
                        planDtosPage.getSize(),
                        planDtosPage.getNumberOfElements()));
    }

    @PreAuthorize("hasAuthority('DELETE_PLAN')")
    @DeleteMapping("{planId}")
    public ResponseEntity<Response<Boolean>> deletePlan(@PathVariable UUID planId) {
        planService.deletePlan(planId);
        return ResponseEntity.ok(
                new Response<>(ApiResponseStatus.SUCCESS, true));
    }
}

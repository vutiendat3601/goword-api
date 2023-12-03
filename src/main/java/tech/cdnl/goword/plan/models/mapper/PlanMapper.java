package tech.cdnl.goword.plan.models.mapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import tech.cdnl.goword.plan.models.dto.PlanDto;
import tech.cdnl.goword.plan.models.entity.Plan;

@Component
public class PlanMapper {
    @Value("${app.static_resource_url}")
    private String staticResourceUrl;

    public PlanDto mapToDto(Plan plan) {

        return new PlanDto(
                plan.getId(),
                plan.getName(),
                plan.getDescription(),
                staticResourceUrl + plan.getThumb(),
                plan.getCreatedAt(),
                List.of());
    }
}

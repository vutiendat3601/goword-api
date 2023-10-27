package tech.cdnl.goword.plan.models.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PlanDto {
    private UUID id;

    private String name;

    private String description;

    private String avatar;
}

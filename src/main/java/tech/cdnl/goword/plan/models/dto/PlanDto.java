package tech.cdnl.goword.plan.models.dto;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.cdnl.goword.exercise.models.dto.ExerciseDto;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PlanDto {
    private UUID id;

    private String name;

    private String description;

    private String thumb;

    private ZonedDateTime createdAt;

    private List<ExerciseDto> exercises;

    @JsonProperty("createdAt")
    public long getCreatedAt() {
        return createdAt == null ? 0 : createdAt.toEpochSecond();
    }
}

package tech.cdnl.goword.exercise.models.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseDto {
    private UUID id;

    private String name;

    private String thumb;

    private String content;

    private ZonedDateTime createdAt;

    @JsonProperty("createdAt")
    public long getCreatedAt() {
        return createdAt == null ? 0 : createdAt.toEpochSecond();
    }
}

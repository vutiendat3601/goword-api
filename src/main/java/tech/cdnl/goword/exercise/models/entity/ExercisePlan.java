package tech.cdnl.goword.exercise.models.entity;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import tech.cdnl.goword.shared.models.AbstractEntity;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exercise_plan")
public class ExercisePlan extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exercise_plan_id_seq")
    @SequenceGenerator(name = "exercise_plan_id_seq", sequenceName = "exercise_plan_id_seq", allocationSize = 1)
    private Long id;

    private UUID exerciseId;

    private String planName;

    private boolean active;

    public ExercisePlan(UUID exerciseId, String planName, boolean active) {
        this.exerciseId = exerciseId;
        this.planName = planName;
        this.active = active;
    }
}

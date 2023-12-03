package tech.cdnl.goword.exercise.models.entity;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import tech.cdnl.goword.shared.models.AbstractEntity;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Where(clause = "deleted = false")
@Entity
@Table(name = "exercises")
public class Exercise extends AbstractEntity {

    @Id
    @Type(type = "pg-uuid")
    @GeneratedValue(generator = "exercises_id_gen")
    @GenericGenerator(name = "exercises_id_gen", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    private String name;

    private String content;

    private String thumb;

    private boolean deleted;

    public Exercise(String name, String content, String thumb) {
        this.name = name;
        this.content = content;
        this.thumb = thumb;
    }
}

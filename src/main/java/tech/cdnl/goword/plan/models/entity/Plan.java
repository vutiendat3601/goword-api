package tech.cdnl.goword.plan.models.entity;

import java.util.UUID;

import javax.persistence.Column;
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
@Table(name = "plans")
public class Plan extends AbstractEntity {

    @Id
    @Type(type = "pg-uuid")
    @GeneratedValue(generator = "plans_id_gen")
    @GenericGenerator(name = "plans_id_gen", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    private String name;

    @Column(name = "`desc`")
    private String description;

    private String avatar;

    private boolean deleted;

    public Plan(String name, String description, String avatar) {
        this.name = name;
        this.description = description;
        this.avatar = avatar;
    }

}

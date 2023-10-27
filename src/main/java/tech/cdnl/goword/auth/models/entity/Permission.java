package tech.cdnl.goword.auth.models.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.http.HttpMethod;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import tech.cdnl.goword.auth.models.PermissionType;
import tech.cdnl.goword.shared.models.AbstractEntity;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "permissions")
@Entity
public class Permission extends AbstractEntity {
    @Id
    @Type(type = "pg-uuid")
    @GeneratedValue(generator = "permissions_id_gen")
    @GenericGenerator(name = "permissions_id_gen", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    private String name;

    @Column(name = "desc")
    private String description;

    private boolean active;

    @Enumerated(EnumType.STRING)
    private PermissionType type;

    private String url;

    @Enumerated(EnumType.STRING)
    private HttpMethod urlMethod;
}

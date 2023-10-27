package tech.cdnl.goword.auth.models.entity;

import java.time.ZonedDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import tech.cdnl.goword.shared.models.AbstractEntity;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sessions")
public class Session extends AbstractEntity {
    @Id
    @Type(type = "pg-uuid")
    @GeneratedValue(generator = "sessions_id_gen")
    @GenericGenerator(name = "sessions_id_gen", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    private UUID userId;

    private ZonedDateTime signedInAt;

    private ZonedDateTime signedOutAt;

    @Column(name = "ref_token")
    private String refreshToken;

    @Column(name = "ref_token_exp_at")
    private Long refreshTokenExpireAt;

    public Session(UUID userId, ZonedDateTime signedInAt) {
        this.userId = userId;
        this.signedInAt = signedInAt;
    }

    public Session(UUID userId, ZonedDateTime signedInAt, String refreshToken, Long refreshTokenExpireAt) {
        this.userId = userId;
        this.signedInAt = signedInAt;
        this.refreshToken = refreshToken;
        this.refreshTokenExpireAt = refreshTokenExpireAt;
    }
}

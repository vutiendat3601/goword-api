package tech.cdnl.goword.user.models.entity;

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

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends AbstractEntity {
    @Id
    @Type(type = "pg-uuid")
    @GeneratedValue(generator = "users_id_gen")
    @GenericGenerator(name = "users_id_gen", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

    @Column(name = "pwd")
    private String password;

    private boolean verified;

    @Column(name = "verif_code")
    private String verificationCode;

    @Column(name = "verif_code_exp_at")
    private Long verificationCodeExpireAt;

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
}

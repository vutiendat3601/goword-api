package tech.cdnl.goword.shared.models;

import java.time.ZonedDateTime;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;
import tech.cdnl.goword.shared.AppConstant;
import tech.cdnl.goword.shared.utils.JwtContext;

@MappedSuperclass
@Data
public abstract class AbstractEntity {
    private String createdBy;

    private String updatedBy;

    @CreationTimestamp
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    private ZonedDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (createdBy == null && updatedBy == null) {
            String username = JwtContext.getUsernameClaim();
            username = username == null ? AppConstant.GUEST_USERNAME : username;
            username = username.equals("") ? AppConstant.SYSTEM_USERNAME : username;
            createdBy = username;
            updatedBy = username;
        }
    }

    @PreUpdate
    public void preUpdate() {
        if (updatedBy == null) {
            String username = JwtContext.getUsernameClaim();
            username = username == null ? AppConstant.GUEST_USERNAME : username;
            username = username.equals("") ? AppConstant.SYSTEM_USERNAME : username;
            updatedBy = username;
        }
    }
}
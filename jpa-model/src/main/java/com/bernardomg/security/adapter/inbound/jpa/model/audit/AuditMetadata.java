
package com.bernardomg.security.adapter.inbound.jpa.model.audit;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import com.bernardomg.security.adapter.inbound.jpa.model.user.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;

@Embeddable
public class AuditMetadata implements Serializable {

    /**
     * Serialization id.
     */
    @Transient
    private static final long serialVersionUID = 6571072545115490466L;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Instant           createdAt;

    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", referencedColumnName = "id", updatable = false)
    private UserEntity        createdBy;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant           updatedAt;

    @LastModifiedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", referencedColumnName = "id")
    private UserEntity        updatedBy;

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        final AuditMetadata other = (AuditMetadata) obj;
        return Objects.equals(createdAt, other.createdAt) && Objects.equals(createdBy, other.createdBy)
                && Objects.equals(updatedAt, other.updatedAt) && Objects.equals(updatedBy, other.updatedBy);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public UserEntity getCreatedBy() {
        return createdBy;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public UserEntity getUpdatedBy() {
        return updatedBy;
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdAt, createdBy, updatedAt, updatedBy);
    }

    @Override
    public String toString() {
        return "AuditMetadata [createdAt=" + createdAt + ", createdBy=" + createdBy + ", updatedAt=" + updatedAt
                + ", updatedBy=" + updatedBy + "]";
    }

}


package com.bernardomg.security.adapter.inbound.jpa.model.audit;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", referencedColumnName = "id", insertable = false, updatable = false)
    private AuditUserEntity   createdBy;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private Long              createdById;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant           updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", referencedColumnName = "id", insertable = false, updatable = false)
    private AuditUserEntity   updatedBy;

    @LastModifiedBy
    @Column(name = "updated_by")
    private Long              updatedById;

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        final AuditMetadata other = (AuditMetadata) obj;
        return Objects.equals(createdAt, other.createdAt) && Objects.equals(createdById, other.createdById)
                && Objects.equals(updatedAt, other.updatedAt) && Objects.equals(updatedById, other.updatedById);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public AuditUserEntity getCreatedBy() {
        return createdBy;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public AuditUserEntity getUpdatedBy() {
        return updatedBy;
    }

    public Long getUpdatedById() {
        return updatedById;
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdAt, createdBy, updatedAt, updatedBy);
    }

    public void setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setCreatedBy(final AuditUserEntity createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreatedById(final Long createdById) {
        this.createdById = createdById;
    }

    public void setUpdatedAt(final Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setUpdatedBy(final AuditUserEntity updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setUpdatedById(final Long updatedById) {
        this.updatedById = updatedById;
    }

    @Override
    public String toString() {
        return "AuditMetadata [createdAt=" + createdAt + ", createdById=" + createdById + ", updatedAt=" + updatedAt
                + ", updatedById=" + updatedById + "]";
    }

}

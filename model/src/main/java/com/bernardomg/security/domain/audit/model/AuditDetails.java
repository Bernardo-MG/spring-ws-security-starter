
package com.bernardomg.security.domain.audit.model;

import java.time.Instant;

public record AuditDetails(Instant createdAt, AuditUser createdBy, Instant updatedAt, AuditUser updatedBy) {

    public AuditDetails() {
        this(null, null, null, null);
    }

    public record AuditUser(String email, String username, String name) {

    }

}

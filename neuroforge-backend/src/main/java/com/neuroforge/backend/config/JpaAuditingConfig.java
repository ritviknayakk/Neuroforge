package com.neuroforge.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Enables Spring Data JPA auditing (@CreatedDate / @LastModifiedDate /
 * 
 * @CreatedBy / @LastModifiedBy) project-wide.
 *
 *            IMPORTANT: your build report notes @EnableJpaAuditing was already
 *            added
 *            directly on NeuroforgeBackendApplication.java. Keep it in exactly
 *            ONE
 *            place — having it on both the main class and here will fail to
 *            start
 *            (duplicate bean definition / conflicting auditor-aware refs).
 *            Recommended:
 *            delete @EnableJpaAuditing from NeuroforgeBackendApplication.java
 *            and let
 *            this config class own it, so auditing config doesn't get lost in
 *            the
 *            middle of application bootstrap code as the project grows.
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfig {
}
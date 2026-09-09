package com.neuroforge.backend.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Supplies the current UserID to Spring Data JPA's @CreatedBy / @LastModifiedBy
 * * Supplies the current UserID to Spring Data JPA's @CreatedBy
 * / @LastModifiedBy
 * fields on entities that declare them.
 *
 * Deliverable #6 (JWT/RBAC) isn't built yet, so there's no real
 * Authentication in the SecurityContext during normal requests — this
 * returns Optional.empty() until then, which just means CreatedByUserID /
 * ModifiedByUserID come back null on save, same as today. Nothing here
 * needs to change once JWT auth lands: the JwtAuthenticationFilter will
 * populate SecurityContextHolder with an Authentication whose principal
 * resolves to a UserID, and this class will start returning it automatically.
 *
 * The expected principal type from the (future) JWT filter is your own
 * UserPrincipal/CustomUserDetails class exposing getUserId(). Adjust the
 * cast below once that class exists — it's stubbed out here so this file
 * compiles standalone today.
 */
@Component("auditorProvider")
public class AuditorAwareImpl implements AuditorAware<Integer> {

    @Override
    public Optional<Integer> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();

        // TODO(deliverable #6): once JwtAuthenticationFilter + a
        // UserPrincipal(userId, email, authorities) class exist, replace
        // this block with:
        // if (principal instanceof UserPrincipal userPrincipal) {
        // return Optional.of(userPrincipal.getUserId());
        // }
        if (principal instanceof Integer userId) {
            return Optional.of(userId);
        }

        return Optional.empty();
    }
}
package com.neuroforge.backend.repository;

import com.neuroforge.backend.entity.UserSessions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserSessionsRepository extends JpaRepository<UserSessions, Long> {

    Optional<UserSessions> findByTokenHash(String tokenHash);

    List<UserSessions> findByUser_UserId(Integer userId);

    List<UserSessions> findByUser_UserIdAndRevokedAtIsNull(Integer userId);

    List<UserSessions> findByUser_UserIdAndRevokedAtIsNullAndExpiresAtAfter(Integer userId, LocalDateTime now);

    Optional<UserSessions> findByTokenHashAndRevokedAtIsNull(String tokenHash);

    Optional<UserSessions> findByTokenHashAndRevokedAtIsNullAndExpiresAtAfter(String tokenHash, LocalDateTime now);

    List<UserSessions> findByExpiresAtBeforeAndRevokedAtIsNull(LocalDateTime now);

    boolean existsByTokenHashAndRevokedAtIsNull(String tokenHash);

    @Modifying
    @Query("UPDATE UserSessions us SET us.revokedAt = :revokedAt WHERE us.user.userId = :userId")
    void revokeAllUserSessions(@Param("userId") Integer userId, @Param("revokedAt") LocalDateTime revokedAt);

    @Modifying
    @Query("UPDATE UserSessions us SET us.revokedAt = :revokedAt WHERE us.user.userId = :userId AND us.revokedAt IS NULL")
    void revokeAllActiveUserSessions(@Param("userId") Integer userId, @Param("revokedAt") LocalDateTime revokedAt);

    @Modifying
    @Query("DELETE FROM UserSessions us WHERE us.expiresAt < :now AND us.revokedAt IS NOT NULL")
    void deleteExpiredAndRevokedSessions(@Param("now") LocalDateTime now);

    long countByUser_UserIdAndRevokedAtIsNull(Integer userId);

    long countByUser_UserIdAndRevokedAtIsNullAndExpiresAtAfter(Integer userId, LocalDateTime now);
}
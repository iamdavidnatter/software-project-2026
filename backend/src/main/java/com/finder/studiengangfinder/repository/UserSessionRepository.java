package com.finder.studiengangfinder.repository;

import com.finder.studiengangfinder.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserSessionRepository extends JpaRepository<UserSession, UUID> {
    Optional<UserSession> findByResultToken(UUID resultToken);
}

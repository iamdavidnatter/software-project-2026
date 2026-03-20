package com.finder.studiengangfinder.service;

import com.finder.studiengangfinder.dto.CreateSessionRequest;
import com.finder.studiengangfinder.dto.SessionDto;
import com.finder.studiengangfinder.entity.UserSession;
import com.finder.studiengangfinder.exception.NotFoundException;
import com.finder.studiengangfinder.repository.UserSessionRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class SessionService {

    private final UserSessionRepository userSessionRepository;

    public SessionService(UserSessionRepository userSessionRepository) {
        this.userSessionRepository = userSessionRepository;
    }

    public SessionDto createSession(CreateSessionRequest request) {
        UserSession session = new UserSession();
        session.setNickname((request.nickname() == null || request.nickname().isBlank()) ? "Gast" : request.nickname().trim());
        session.setAvailableHoursPerWeek(request.availableHoursPerWeek() == null ? 15 : request.availableHoursPerWeek());
        session.setCreatedAt(OffsetDateTime.now());
        session.setResultToken(UUID.randomUUID());
        userSessionRepository.save(session);
        return toDto(session);
    }

    public UserSession getSession(UUID sessionId) {
        return userSessionRepository.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("Session nicht gefunden."));
    }

    public UserSession getSessionByToken(UUID token) {
        return userSessionRepository.findByResultToken(token)
                .orElseThrow(() -> new NotFoundException("Ergebnis-Link nicht gefunden."));
    }

    public SessionDto toDto(UserSession session) {
        return new SessionDto(
                session.getId(),
                session.getResultToken(),
                session.getNickname(),
                session.getAvailableHoursPerWeek(),
                session.getCreatedAt()
        );
    }
}

package com.finder.studiengangfinder.controller;

import com.finder.studiengangfinder.dto.CreateSessionRequest;
import com.finder.studiengangfinder.dto.SessionDto;
import com.finder.studiengangfinder.dto.SubmitAnswersRequest;
import com.finder.studiengangfinder.service.MatchingService;
import com.finder.studiengangfinder.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionService sessionService;
    private final MatchingService matchingService;

    public SessionController(SessionService sessionService, MatchingService matchingService) {
        this.sessionService = sessionService;
        this.matchingService = matchingService;
    }

    @PostMapping
    public SessionDto createSession(@Valid @RequestBody CreateSessionRequest request) {
        return sessionService.createSession(request);
    }

    @PostMapping("/{sessionId}/answers")
    public com.finder.studiengangfinder.dto.ResultDto submitAnswers(
            @PathVariable UUID sessionId,
            @Valid @RequestBody SubmitAnswersRequest request
    ) {
        return matchingService.submitAnswers(sessionService.getSession(sessionId), request.answers());
    }
}

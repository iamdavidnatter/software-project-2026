package com.finder.studiengangfinder.controller;

import com.finder.studiengangfinder.dto.FavoriteUpdateRequest;
import com.finder.studiengangfinder.dto.ResultDto;
import com.finder.studiengangfinder.entity.UserSession;
import com.finder.studiengangfinder.service.FavoriteService;
import com.finder.studiengangfinder.service.MatchingService;
import com.finder.studiengangfinder.service.PdfService;
import com.finder.studiengangfinder.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/results")
public class ResultController {

    private final SessionService sessionService;
    private final MatchingService matchingService;
    private final FavoriteService favoriteService;
    private final PdfService pdfService;

    public ResultController(
            SessionService sessionService,
            MatchingService matchingService,
            FavoriteService favoriteService,
            PdfService pdfService
    ) {
        this.sessionService = sessionService;
        this.matchingService = matchingService;
        this.favoriteService = favoriteService;
        this.pdfService = pdfService;
    }

    @GetMapping("/{token}")
    public ResultDto getResult(@PathVariable UUID token) {
        return matchingService.toResultDto(sessionService.getSessionByToken(token));
    }

    @PutMapping("/{token}/favorites")
    public Map<String, List<Long>> updateFavorites(@PathVariable UUID token, @Valid @RequestBody FavoriteUpdateRequest request) {
        UserSession session = sessionService.getSessionByToken(token);
        return Map.of("favoriteProgramIds", favoriteService.updateFavorites(session, request.programIds()));
    }

    @GetMapping("/{token}/pdf")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable UUID token) {
        ResultDto result = matchingService.toResultDto(sessionService.getSessionByToken(token));
        byte[] pdf = pdfService.generateResultPdf(result);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename("studiengangs-finder-ergebnis.pdf").build().toString())
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}

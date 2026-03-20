package com.finder.studiengangfinder.service;

import com.finder.studiengangfinder.dto.AdminDashboardDto;
import com.finder.studiengangfinder.dto.ProgramInterestSummaryDto;
import com.finder.studiengangfinder.dto.RecentResultDto;
import com.finder.studiengangfinder.entity.Favorite;
import com.finder.studiengangfinder.entity.ResultRecommendation;
import com.finder.studiengangfinder.entity.UserSession;
import com.finder.studiengangfinder.repository.UserSessionRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminDashboardService {

    private final UserSessionRepository userSessionRepository;

    public AdminDashboardService(UserSessionRepository userSessionRepository) {
        this.userSessionRepository = userSessionRepository;
    }

    public AdminDashboardDto getDashboard() {
        List<UserSession> sessions = userSessionRepository.findAll();
        long completedResults = sessions.stream().filter(session -> session.getResult() != null).count();
        long totalFavorites = sessions.stream().mapToLong(session -> session.getFavorites().size()).sum();

        Map<String, Long> modelCounts = new HashMap<>();
        Map<Long, ProgramAggregate> programAggregates = new HashMap<>();

        for (UserSession session : sessions) {
            if (session.getResult() == null || session.getResult().getRecommendations().isEmpty()) {
                continue;
            }

            ResultRecommendation topRecommendation = session.getResult().getRecommendations().stream()
                    .max(Comparator.comparing(ResultRecommendation::getScore))
                    .orElse(null);
            if (topRecommendation != null) {
                modelCounts.merge(topRecommendation.getStudyProgram().getStudyModel().name(), 1L, Long::sum);
                ProgramAggregate aggregate = programAggregates.computeIfAbsent(
                        topRecommendation.getStudyProgram().getId(),
                        ignored -> new ProgramAggregate(topRecommendation.getStudyProgram().getId(), topRecommendation.getStudyProgram().getName())
                );
                aggregate.topRecommendationCount++;
                aggregate.scoreSum += topRecommendation.getScore();
            }

            for (Favorite favorite : session.getFavorites()) {
                ProgramAggregate aggregate = programAggregates.computeIfAbsent(
                        favorite.getStudyProgram().getId(),
                        ignored -> new ProgramAggregate(favorite.getStudyProgram().getId(), favorite.getStudyProgram().getName())
                );
                aggregate.favoriteCount++;
            }
        }

        String mostPopularStudyModel = modelCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Noch keine Daten");

        List<ProgramInterestSummaryDto> topPrograms = programAggregates.values().stream()
                .sorted(Comparator.comparingLong(ProgramAggregate::combinedRank).reversed())
                .limit(6)
                .map(aggregate -> new ProgramInterestSummaryDto(
                        aggregate.programId,
                        aggregate.programName,
                        aggregate.topRecommendationCount,
                        aggregate.topRecommendationCount == 0 ? 0 : (double) aggregate.scoreSum / aggregate.topRecommendationCount,
                        aggregate.favoriteCount
                ))
                .toList();

        List<RecentResultDto> recentResults = sessions.stream()
                .filter(session -> session.getResult() != null && !session.getResult().getRecommendations().isEmpty())
                .sorted(Comparator.comparing((UserSession session) -> session.getResult().getGeneratedAt()).reversed())
                .limit(12)
                .map(session -> {
                    ResultRecommendation top = session.getResult().getRecommendations().stream()
                            .max(Comparator.comparing(ResultRecommendation::getScore))
                            .orElseThrow();
                    return new RecentResultDto(
                            session.getNickname(),
                            session.getResult().getGeneratedAt(),
                            session.getResultToken(),
                            top.getStudyProgram().getName(),
                            top.getScore(),
                            session.getAvailableHoursPerWeek()
                    );
                })
                .toList();

        return new AdminDashboardDto(
                sessions.size(),
                completedResults,
                totalFavorites,
                mostPopularStudyModel,
                topPrograms,
                recentResults
        );
    }

    private static class ProgramAggregate {
        private final Long programId;
        private final String programName;
        private long topRecommendationCount;
        private int scoreSum;
        private long favoriteCount;

        private ProgramAggregate(Long programId, String programName) {
            this.programId = programId;
            this.programName = programName;
        }

        private long combinedRank() {
            return (topRecommendationCount * 10) + favoriteCount;
        }
    }
}

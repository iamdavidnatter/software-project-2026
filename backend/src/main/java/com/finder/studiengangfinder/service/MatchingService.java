package com.finder.studiengangfinder.service;

import com.finder.studiengangfinder.dto.AnswerSubmissionItemDto;
import com.finder.studiengangfinder.dto.RecommendationDto;
import com.finder.studiengangfinder.dto.ResultDto;
import com.finder.studiengangfinder.entity.Answer;
import com.finder.studiengangfinder.entity.Favorite;
import com.finder.studiengangfinder.entity.Question;
import com.finder.studiengangfinder.entity.Result;
import com.finder.studiengangfinder.entity.ResultRecommendation;
import com.finder.studiengangfinder.entity.StudyProgram;
import com.finder.studiengangfinder.entity.UserSession;
import com.finder.studiengangfinder.exception.BadRequestException;
import com.finder.studiengangfinder.repository.QuestionRepository;
import com.finder.studiengangfinder.repository.ResultRepository;
import com.finder.studiengangfinder.repository.StudyProgramRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MatchingService {

    private final QuestionRepository questionRepository;
    private final StudyProgramRepository studyProgramRepository;
    private final ResultRepository resultRepository;
    private final StudyProgramService studyProgramService;

    public MatchingService(
            QuestionRepository questionRepository,
            StudyProgramRepository studyProgramRepository,
            ResultRepository resultRepository,
            StudyProgramService studyProgramService
    ) {
        this.questionRepository = questionRepository;
        this.studyProgramRepository = studyProgramRepository;
        this.resultRepository = resultRepository;
        this.studyProgramService = studyProgramService;
    }

    @Transactional
    public ResultDto submitAnswers(UserSession session, List<AnswerSubmissionItemDto> answerDtos) {
        Map<String, Question> questionsByKey = questionRepository.findAllByOrderBySortOrderAsc().stream()
                .collect(Collectors.toMap(Question::getQuestionKey, question -> question));

        session.getAnswers().clear();
        List<Answer> persistedAnswers = new ArrayList<>();

        for (AnswerSubmissionItemDto item : answerDtos) {
            Question question = questionsByKey.get(item.questionKey());
            if (question == null) {
                throw new BadRequestException("Unbekannte Frage: " + item.questionKey());
            }
            Answer answer = new Answer();
            answer.setSession(session);
            answer.setQuestion(question);
            answer.setScaleValue(item.scaleValue());
            answer.setSelectedValues(item.selectedValues() == null ? null : String.join(",", item.selectedValues()));
            persistedAnswers.add(answer);
        }
        session.getAnswers().addAll(persistedAnswers);

        CandidateProfile profile = CandidateProfile.from(session);
        List<ResultRecommendation> recommendations = studyProgramRepository.findAll().stream()
                .map(program -> scoreProgram(profile, program))
                .sorted(Comparator.comparing(ResultRecommendation::getScore).reversed())
                .toList();

        Result result = session.getResult() == null ? new Result() : session.getResult();
        result.setSession(session);
        result.setGeneratedAt(OffsetDateTime.now());
        result.setProfileSummary(profile.profileSummary());
        result.getRecommendations().clear();
        recommendations.forEach(rec -> rec.setResult(result));
        result.getRecommendations().addAll(recommendations);
        session.setResult(result);
        resultRepository.save(result);

        return toResultDto(session);
    }

    public ResultDto toResultDto(UserSession session) {
        if (session.getResult() == null) {
            throw new BadRequestException("Fuer diese Session wurde noch kein Ergebnis berechnet.");
        }
        CandidateProfile profile = CandidateProfile.from(session);
        Set<Long> favoriteIds = session.getFavorites().stream()
                .map(favorite -> favorite.getStudyProgram().getId())
                .collect(Collectors.toSet());

        List<RecommendationDto> recommendations = session.getResult().getRecommendations().stream()
                .sorted(Comparator.comparing(ResultRecommendation::getScore).reversed())
                .map(rec -> toRecommendationDto(session, rec, favoriteIds))
                .toList();

        return new ResultDto(
                session.getResultToken(),
                session.getNickname(),
                session.getResult().getGeneratedAt(),
                session.getAvailableHoursPerWeek(),
                session.getResult().getProfileSummary(),
                profile.interests().stream().sorted().toList(),
                profile.skills().stream().sorted().toList(),
                profile.preferences().stream().sorted().toList(),
                favoriteIds.stream().sorted().toList(),
                recommendations.isEmpty() ? null : recommendations.getFirst(),
                recommendations
        );
    }

    private RecommendationDto toRecommendationDto(UserSession session, ResultRecommendation recommendation, Set<Long> favoriteIds) {
        StudyProgram program = recommendation.getStudyProgram();
        List<String> highlights = List.of(
                "Arbeitsaufwand ca. " + program.getEstimatedWorkloadHoursPerWeek() + " Std./Woche",
                "Studienmodell: " + program.getStudyModel().name(),
                "Theoriegrad: " + program.getTheoryLevel() + "/5"
        );
        return new RecommendationDto(
                program.getId(),
                program.getName(),
                recommendation.getScore(),
                recommendation.getRationale(),
                recommendation.getDynamicRiskSummary(),
                program.getEstimatedWorkloadHoursPerWeek(),
                session.getAvailableHoursPerWeek(),
                favoriteIds.contains(program.getId()),
                studyProgramService.toDto(program),
                highlights
        );
    }

    private ResultRecommendation scoreProgram(CandidateProfile profile, StudyProgram program) {
        double interestScore = overlapScore(profile.interests(), new HashSet<>(program.getInterestTags()));
        double skillScore = overlapScore(profile.skills(), new HashSet<>(program.getSkillTags()));
        double preferenceScore = overlapScore(profile.preferences(), new HashSet<>(program.getPreferenceTags()));
        double theoryFit = 1 - (Math.abs(profile.theoryPreference() - program.getTheoryLevel()) / 4.0);
        double flexibilityFit = Math.min(1.0, profile.availableHours() / (double) program.getEstimatedWorkloadHoursPerWeek());
        double modelFit = profile.preferences().contains(program.getStudyModel().name().toLowerCase()) ? 1.0 : 0.55;
        double experienceFit = profile.experienceLevel() >= 3 && program.getPreferenceTags().contains("career_switch")
                ? 0.95 : Math.min(1.0, 0.55 + (profile.experienceLevel() / 5.0));

        int score = (int) Math.round((interestScore * 0.30
                + skillScore * 0.25
                + preferenceScore * 0.15
                + theoryFit * 0.10
                + flexibilityFit * 0.10
                + modelFit * 0.05
                + experienceFit * 0.05) * 100);

        ResultRecommendation recommendation = new ResultRecommendation();
        recommendation.setStudyProgram(program);
        recommendation.setScore(Math.max(40, Math.min(98, score)));
        recommendation.setRationale(buildRationale(profile, program, interestScore, skillScore, preferenceScore));
        recommendation.setDynamicRiskSummary(buildDynamicRisk(profile, program));
        return recommendation;
    }

    private String buildRationale(CandidateProfile profile, StudyProgram program, double interestScore, double skillScore, double preferenceScore) {
        List<String> reasons = new ArrayList<>();
        if (interestScore > 0.5) {
            reasons.add("deine Interessen passen stark zu den Themenfeldern " + String.join(", ", program.getInterestTags()));
        }
        if (skillScore > 0.4) {
            reasons.add("du bringst bereits relevante Vorkenntnisse in " + String.join(", ", intersect(profile.skills(), new HashSet<>(program.getSkillTags()))) + " mit");
        }
        if (preferenceScore > 0.4) {
            reasons.add("das Studienmodell und die Lernform entsprechen deinen Praeferenzen");
        }
        if (reasons.isEmpty()) {
            reasons.add("das Programm bietet eine ausgewogene Mischung aus Einstieg und Weiterentwicklung fuer dein Profil");
        }
        return "Match, weil " + String.join("; ", reasons) + ".";
    }

    private String buildDynamicRisk(CandidateProfile profile, StudyProgram program) {
        List<String> risks = new ArrayList<>();
        if (profile.availableHours() < program.getEstimatedWorkloadHoursPerWeek()) {
            risks.add("dein aktuelles Zeitbudget liegt unter dem typischen Aufwand");
        }
        if (program.getTheoryLevel() >= 4 && profile.theoryPreference() <= 2) {
            risks.add("der Theorieanteil koennte fuer dich hoeher sein als gewuenscht");
        }
        Set<String> missingSkills = new HashSet<>(program.getSkillTags());
        missingSkills.removeAll(profile.skills());
        if (missingSkills.size() >= 2) {
            risks.add("einige empfohlene Vorkenntnisse fehlen noch: " + String.join(", ", missingSkills.stream().limit(3).toList()));
        }
        if (risks.isEmpty()) {
            return "Fuer dein Profil sind aktuell keine kritischen Huerden sichtbar.";
        }
        return String.join(". ", risks) + ".";
    }

    private double overlapScore(Set<String> candidateTags, Set<String> programTags) {
        if (programTags.isEmpty()) {
            return 0.5;
        }
        Set<String> intersection = new HashSet<>(programTags);
        intersection.retainAll(candidateTags);
        return intersection.size() / (double) programTags.size();
    }

    private Set<String> intersect(Set<String> left, Set<String> right) {
        Set<String> result = new HashSet<>(left);
        result.retainAll(right);
        return result;
    }

    private record CandidateProfile(
            Set<String> interests,
            Set<String> skills,
            Set<String> preferences,
            int theoryPreference,
            int experienceLevel,
            int availableHours
    ) {
        static CandidateProfile from(UserSession session) {
            Map<String, Answer> answerMap = new HashMap<>();
            session.getAnswers().forEach(answer -> answerMap.put(answer.getQuestion().getQuestionKey(), answer));

            Set<String> interests = new HashSet<>();
            Set<String> skills = new HashSet<>();
            Set<String> preferences = new HashSet<>();

            mergeValues(interests, answerMap.get("interest_topics"));
            mergeValues(interests, answerMap.get("interest_tasks"));
            mergeValues(interests, answerMap.get("career_goal"));
            mergeValues(skills, answerMap.get("existing_skills"));
            mergeValues(preferences, answerMap.get("study_model"));
            mergeValues(preferences, answerMap.get("learning_style"));
            mergeValues(preferences, answerMap.get("international_support"));
            mergeValues(preferences, answerMap.get("career_goal"));

            int theoryPreference = scale(answerMap.get("theory_vs_practice"), 3);
            int experienceLevel = scale(answerMap.get("work_experience"), 2);
            int mathConfidence = scale(answerMap.get("math_confidence"), 3);
            int programmingConfidence = scale(answerMap.get("programming_confidence"), 3);

            if (mathConfidence >= 4) {
                skills.add("math");
            }
            if (programmingConfidence >= 4) {
                skills.add("programming");
            }

            preferences.add(session.getAvailableHoursPerWeek() >= 20 ? "high_availability" : "limited_time");

            return new CandidateProfile(interests, skills, preferences, theoryPreference, experienceLevel, session.getAvailableHoursPerWeek());
        }

        String profileSummary() {
            return "Profil mit Fokus auf " + String.join(", ", interests.stream().sorted().limit(4).toList())
                    + "; vorhandene Skills: " + String.join(", ", skills.stream().sorted().limit(5).toList())
                    + "; bevorzugte Rahmenbedingungen: " + String.join(", ", preferences.stream().sorted().limit(4).toList()) + ".";
        }

        private static int scale(Answer answer, int fallback) {
            return answer == null || answer.getScaleValue() == null ? fallback : answer.getScaleValue();
        }

        private static void mergeValues(Set<String> target, Answer answer) {
            if (answer == null || answer.getSelectedValues() == null || answer.getSelectedValues().isBlank()) {
                return;
            }
            for (String value : answer.getSelectedValues().split(",")) {
                if (!value.isBlank()) {
                    target.add(value.trim());
                }
            }
        }
    }
}

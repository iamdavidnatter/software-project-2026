package com.finder.studiengangfinder.service;

import com.finder.studiengangfinder.dto.QuestionDto;
import com.finder.studiengangfinder.dto.QuestionOptionDto;
import com.finder.studiengangfinder.dto.QuestionnaireDto;
import com.finder.studiengangfinder.entity.Question;
import com.finder.studiengangfinder.repository.QuestionRepository;
import org.springframework.stereotype.Service;

@Service
public class QuestionnaireService {

    private final QuestionRepository questionRepository;

    public QuestionnaireService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public QuestionnaireDto getQuestionnaire() {
        return new QuestionnaireDto(
                "Studiengangs-Finder",
                "Finde in wenigen Minuten den passenden Studiengang fuer deine Interessen, Skills und Lebensrealitaet.",
                questionRepository.findAllByOrderBySortOrderAsc().stream().map(this::toDto).toList()
        );
    }

    private QuestionDto toDto(Question question) {
        return new QuestionDto(
                question.getId(),
                question.getQuestionKey(),
                question.getSortOrder(),
                question.getTitle(),
                question.getDescription(),
                question.getType().name(),
                question.isRequiredQuestion(),
                question.getMinScale(),
                question.getMaxScale(),
                question.getOptions().stream()
                        .map(option -> new QuestionOptionDto(option.getId(), option.getLabel(), option.getOptionValue()))
                        .toList()
        );
    }
}

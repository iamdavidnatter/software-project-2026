package com.finder.studiengangfinder.controller;

import com.finder.studiengangfinder.dto.QuestionnaireDto;
import com.finder.studiengangfinder.service.QuestionnaireService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/questionnaire")
public class QuestionnaireController {

    private final QuestionnaireService questionnaireService;

    public QuestionnaireController(QuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    @GetMapping
    public QuestionnaireDto getQuestionnaire() {
        return questionnaireService.getQuestionnaire();
    }
}

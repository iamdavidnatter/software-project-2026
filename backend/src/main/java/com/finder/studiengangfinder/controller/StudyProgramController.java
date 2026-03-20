package com.finder.studiengangfinder.controller;

import com.finder.studiengangfinder.dto.CompareProgramsRequest;
import com.finder.studiengangfinder.dto.StudyProgramDto;
import com.finder.studiengangfinder.service.StudyProgramService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/study-programs")
public class StudyProgramController {

    private final StudyProgramService studyProgramService;

    public StudyProgramController(StudyProgramService studyProgramService) {
        this.studyProgramService = studyProgramService;
    }

    @GetMapping
    public List<StudyProgramDto> getPrograms() {
        return studyProgramService.getAllPrograms();
    }

    @PostMapping("/compare")
    public List<StudyProgramDto> comparePrograms(@Valid @RequestBody CompareProgramsRequest request) {
        return studyProgramService.comparePrograms(request.programIds());
    }
}

package com.finder.studiengangfinder.service;

import com.finder.studiengangfinder.dto.DailyRoutineEntryDto;
import com.finder.studiengangfinder.dto.InternationalSupportInfoDto;
import com.finder.studiengangfinder.dto.SimpleListItemDto;
import com.finder.studiengangfinder.dto.StudyProgramDto;
import com.finder.studiengangfinder.entity.StudyProgram;
import com.finder.studiengangfinder.repository.StudyProgramRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudyProgramService {

    private final StudyProgramRepository studyProgramRepository;

    public StudyProgramService(StudyProgramRepository studyProgramRepository) {
        this.studyProgramRepository = studyProgramRepository;
    }

    public List<StudyProgramDto> getAllPrograms() {
        return studyProgramRepository.findAll().stream().map(this::toDto).toList();
    }

    public List<StudyProgramDto> comparePrograms(List<Long> programIds) {
        return studyProgramRepository.findByIdIn(programIds).stream().map(this::toDto).toList();
    }

    public StudyProgramDto toDto(StudyProgram program) {
        return new StudyProgramDto(
                program.getId(),
                program.getName(),
                program.getShortDescription(),
                program.getStudyModel().name(),
                program.getDurationMonths(),
                program.getEstimatedWorkloadHoursPerWeek(),
                program.getTheoryLevel(),
                program.getFlexibilityLevel(),
                program.getInterestTags(),
                program.getSkillTags(),
                program.getPreferenceTags(),
                program.getContents().stream().map(item -> new SimpleListItemDto(item.getTitle(), item.getDescription())).toList(),
                program.getCareerPaths().stream().map(item -> new SimpleListItemDto(item.getTitle(), item.getDescription())).toList(),
                program.getRisks().stream().map(item -> new SimpleListItemDto(item.getTitle(), item.getDescription())).toList(),
                program.getApplicationChecklistItems().stream().map(item -> item.getItem()).toList(),
                program.getDailyRoutineEntries().stream().map(item -> new DailyRoutineEntryDto(item.getTimeSlot(), item.getTitle(), item.getDescription())).toList(),
                new InternationalSupportInfoDto(
                        program.getInternationalSupportInfo().getApplicationInfo(),
                        program.getInternationalSupportInfo().getFinancingInfo(),
                        program.getInternationalSupportInfo().getResidenceInfo(),
                        program.getInternationalSupportInfo().getLanguageRequirements()
                )
        );
    }
}

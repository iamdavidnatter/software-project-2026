package com.finder.studiengangfinder.repository;

import com.finder.studiengangfinder.entity.StudyProgram;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyProgramRepository extends JpaRepository<StudyProgram, Long> {
    List<StudyProgram> findByIdIn(List<Long> ids);
}

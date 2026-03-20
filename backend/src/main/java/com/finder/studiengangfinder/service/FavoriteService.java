package com.finder.studiengangfinder.service;

import com.finder.studiengangfinder.entity.Favorite;
import com.finder.studiengangfinder.entity.StudyProgram;
import com.finder.studiengangfinder.entity.UserSession;
import com.finder.studiengangfinder.repository.FavoriteRepository;
import com.finder.studiengangfinder.repository.StudyProgramRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StudyProgramRepository studyProgramRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, StudyProgramRepository studyProgramRepository) {
        this.favoriteRepository = favoriteRepository;
        this.studyProgramRepository = studyProgramRepository;
    }

    @Transactional
    public List<Long> updateFavorites(UserSession session, List<Long> programIds) {
        favoriteRepository.deleteBySession(session);
        List<StudyProgram> programs = studyProgramRepository.findByIdIn(programIds);
        for (StudyProgram program : programs) {
            Favorite favorite = new Favorite();
            favorite.setSession(session);
            favorite.setStudyProgram(program);
            favoriteRepository.save(favorite);
        }
        return programs.stream().map(StudyProgram::getId).toList();
    }

    public Set<Long> getFavorites(UserSession session) {
        return favoriteRepository.findBySession(session).stream()
                .map(favorite -> favorite.getStudyProgram().getId())
                .collect(Collectors.toSet());
    }
}

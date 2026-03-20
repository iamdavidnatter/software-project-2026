package com.finder.studiengangfinder.repository;

import com.finder.studiengangfinder.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllByOrderBySortOrderAsc();
    Optional<Question> findByQuestionKey(String questionKey);
}

package com.finder.studiengangfinder.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "result_recommendations")
public class ResultRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "result_id")
    private Result result;

    @ManyToOne(optional = false)
    @JoinColumn(name = "study_program_id")
    private StudyProgram studyProgram;

    @Column(nullable = false)
    private Integer score;

    @Column(nullable = false, length = 1200)
    private String rationale;

    @Column(nullable = false, length = 1200)
    private String dynamicRiskSummary;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public StudyProgram getStudyProgram() {
        return studyProgram;
    }

    public void setStudyProgram(StudyProgram studyProgram) {
        this.studyProgram = studyProgram;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getRationale() {
        return rationale;
    }

    public void setRationale(String rationale) {
        this.rationale = rationale;
    }

    public String getDynamicRiskSummary() {
        return dynamicRiskSummary;
    }

    public void setDynamicRiskSummary(String dynamicRiskSummary) {
        this.dynamicRiskSummary = dynamicRiskSummary;
    }
}

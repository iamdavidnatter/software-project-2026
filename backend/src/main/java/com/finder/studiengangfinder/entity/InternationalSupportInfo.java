package com.finder.studiengangfinder.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "international_support_info")
public class InternationalSupportInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "study_program_id", unique = true)
    private StudyProgram studyProgram;

    @Column(nullable = false, length = 1000)
    private String applicationInfo;

    @Column(nullable = false, length = 1000)
    private String financingInfo;

    @Column(nullable = false, length = 1000)
    private String residenceInfo;

    @Column(nullable = false, length = 1000)
    private String languageRequirements;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StudyProgram getStudyProgram() {
        return studyProgram;
    }

    public void setStudyProgram(StudyProgram studyProgram) {
        this.studyProgram = studyProgram;
    }

    public String getApplicationInfo() {
        return applicationInfo;
    }

    public void setApplicationInfo(String applicationInfo) {
        this.applicationInfo = applicationInfo;
    }

    public String getFinancingInfo() {
        return financingInfo;
    }

    public void setFinancingInfo(String financingInfo) {
        this.financingInfo = financingInfo;
    }

    public String getResidenceInfo() {
        return residenceInfo;
    }

    public void setResidenceInfo(String residenceInfo) {
        this.residenceInfo = residenceInfo;
    }

    public String getLanguageRequirements() {
        return languageRequirements;
    }

    public void setLanguageRequirements(String languageRequirements) {
        this.languageRequirements = languageRequirements;
    }
}

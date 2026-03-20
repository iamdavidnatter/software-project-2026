package com.finder.studiengangfinder.entity;

import com.finder.studiengangfinder.enums.StudyModel;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "study_programs")
public class StudyProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, length = 1200)
    private String shortDescription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StudyModel studyModel;

    @Column(nullable = false)
    private Integer durationMonths;

    @Column(nullable = false)
    private Integer estimatedWorkloadHoursPerWeek;

    @Column(nullable = false)
    private Integer theoryLevel;

    @Column(nullable = false)
    private Integer flexibilityLevel;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "study_program_interest_tags", joinColumns = @JoinColumn(name = "study_program_id"))
    @Column(name = "tag")
    private List<String> interestTags = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "study_program_skill_tags", joinColumns = @JoinColumn(name = "study_program_id"))
    @Column(name = "tag")
    private List<String> skillTags = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "study_program_preference_tags", joinColumns = @JoinColumn(name = "study_program_id"))
    @Column(name = "tag")
    private List<String> preferenceTags = new ArrayList<>();

    @OneToMany(mappedBy = "studyProgram", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC")
    private List<StudyProgramContent> contents = new ArrayList<>();

    @OneToMany(mappedBy = "studyProgram", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC")
    private List<CareerPath> careerPaths = new ArrayList<>();

    @OneToMany(mappedBy = "studyProgram", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC")
    private List<RiskItem> risks = new ArrayList<>();

    @OneToMany(mappedBy = "studyProgram", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC")
    private List<ApplicationChecklistItem> applicationChecklistItems = new ArrayList<>();

    @OneToMany(mappedBy = "studyProgram", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC")
    private List<DailyRoutineEntry> dailyRoutineEntries = new ArrayList<>();

    @OneToOne(mappedBy = "studyProgram", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private InternationalSupportInfo internationalSupportInfo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public StudyModel getStudyModel() {
        return studyModel;
    }

    public void setStudyModel(StudyModel studyModel) {
        this.studyModel = studyModel;
    }

    public Integer getDurationMonths() {
        return durationMonths;
    }

    public void setDurationMonths(Integer durationMonths) {
        this.durationMonths = durationMonths;
    }

    public Integer getEstimatedWorkloadHoursPerWeek() {
        return estimatedWorkloadHoursPerWeek;
    }

    public void setEstimatedWorkloadHoursPerWeek(Integer estimatedWorkloadHoursPerWeek) {
        this.estimatedWorkloadHoursPerWeek = estimatedWorkloadHoursPerWeek;
    }

    public Integer getTheoryLevel() {
        return theoryLevel;
    }

    public void setTheoryLevel(Integer theoryLevel) {
        this.theoryLevel = theoryLevel;
    }

    public Integer getFlexibilityLevel() {
        return flexibilityLevel;
    }

    public void setFlexibilityLevel(Integer flexibilityLevel) {
        this.flexibilityLevel = flexibilityLevel;
    }

    public List<String> getInterestTags() {
        return interestTags;
    }

    public void setInterestTags(List<String> interestTags) {
        this.interestTags = interestTags;
    }

    public List<String> getSkillTags() {
        return skillTags;
    }

    public void setSkillTags(List<String> skillTags) {
        this.skillTags = skillTags;
    }

    public List<String> getPreferenceTags() {
        return preferenceTags;
    }

    public void setPreferenceTags(List<String> preferenceTags) {
        this.preferenceTags = preferenceTags;
    }

    public List<StudyProgramContent> getContents() {
        return contents;
    }

    public void setContents(List<StudyProgramContent> contents) {
        this.contents = contents;
    }

    public List<CareerPath> getCareerPaths() {
        return careerPaths;
    }

    public void setCareerPaths(List<CareerPath> careerPaths) {
        this.careerPaths = careerPaths;
    }

    public List<RiskItem> getRisks() {
        return risks;
    }

    public void setRisks(List<RiskItem> risks) {
        this.risks = risks;
    }

    public List<ApplicationChecklistItem> getApplicationChecklistItems() {
        return applicationChecklistItems;
    }

    public void setApplicationChecklistItems(List<ApplicationChecklistItem> applicationChecklistItems) {
        this.applicationChecklistItems = applicationChecklistItems;
    }

    public List<DailyRoutineEntry> getDailyRoutineEntries() {
        return dailyRoutineEntries;
    }

    public void setDailyRoutineEntries(List<DailyRoutineEntry> dailyRoutineEntries) {
        this.dailyRoutineEntries = dailyRoutineEntries;
    }

    public InternationalSupportInfo getInternationalSupportInfo() {
        return internationalSupportInfo;
    }

    public void setInternationalSupportInfo(InternationalSupportInfo internationalSupportInfo) {
        this.internationalSupportInfo = internationalSupportInfo;
    }
}

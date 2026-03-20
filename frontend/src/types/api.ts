export type QuestionOption = {
  id: number;
  label: string;
  value: string;
};

export type Question = {
  id: number;
  key: string;
  sortOrder: number;
  title: string;
  description: string;
  type: "SINGLE_CHOICE" | "MULTIPLE_CHOICE" | "SCALE";
  required: boolean;
  minScale?: number;
  maxScale?: number;
  options: QuestionOption[];
};

export type Questionnaire = {
  title: string;
  subtitle: string;
  questions: Question[];
};

export type Session = {
  sessionId: string;
  resultToken: string;
  nickname: string;
  availableHoursPerWeek: number;
  createdAt: string;
};

export type ListItem = {
  title: string;
  description: string;
};

export type DailyRoutineEntry = {
  timeSlot: string;
  title: string;
  description: string;
};

export type InternationalSupportInfo = {
  applicationInfo: string;
  financingInfo: string;
  residenceInfo: string;
  languageRequirements: string;
};

export type StudyProgram = {
  id: number;
  name: string;
  shortDescription: string;
  studyModel: "FULL_TIME" | "PART_TIME" | "HYBRID";
  durationMonths: number;
  estimatedWorkloadHoursPerWeek: number;
  theoryLevel: number;
  flexibilityLevel: number;
  interestTags: string[];
  skillTags: string[];
  preferenceTags: string[];
  contents: ListItem[];
  careerPaths: ListItem[];
  risks: ListItem[];
  applicationChecklist: string[];
  dailyRoutine: DailyRoutineEntry[];
  internationalSupport: InternationalSupportInfo;
};

export type Recommendation = {
  programId: number;
  programName: string;
  score: number;
  rationale: string;
  dynamicRiskSummary: string;
  estimatedWorkloadHoursPerWeek: number;
  availableHoursPerWeek: number;
  favorite: boolean;
  details: StudyProgram;
  highlights: string[];
};

export type Result = {
  token: string;
  nickname: string;
  generatedAt: string;
  availableHoursPerWeek: number;
  profileSummary: string;
  selectedInterests: string[];
  selectedSkills: string[];
  selectedPreferences: string[];
  favoriteProgramIds: number[];
  topRecommendation: Recommendation | null;
  recommendations: Recommendation[];
};

export type AnswerPayload = {
  questionKey: string;
  selectedValues?: string[];
  scaleValue?: number;
};

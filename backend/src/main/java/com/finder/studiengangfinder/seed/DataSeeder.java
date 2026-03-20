package com.finder.studiengangfinder.seed;

import com.finder.studiengangfinder.entity.ApplicationChecklistItem;
import com.finder.studiengangfinder.entity.CareerPath;
import com.finder.studiengangfinder.entity.DailyRoutineEntry;
import com.finder.studiengangfinder.entity.InternationalSupportInfo;
import com.finder.studiengangfinder.entity.Question;
import com.finder.studiengangfinder.entity.QuestionOption;
import com.finder.studiengangfinder.entity.RiskItem;
import com.finder.studiengangfinder.entity.StudyProgram;
import com.finder.studiengangfinder.entity.StudyProgramContent;
import com.finder.studiengangfinder.enums.QuestionType;
import com.finder.studiengangfinder.enums.StudyModel;
import com.finder.studiengangfinder.repository.QuestionRepository;
import com.finder.studiengangfinder.repository.StudyProgramRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final StudyProgramRepository studyProgramRepository;
    private final QuestionRepository questionRepository;

    public DataSeeder(StudyProgramRepository studyProgramRepository, QuestionRepository questionRepository) {
        this.studyProgramRepository = studyProgramRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (studyProgramRepository.count() == 0) {
            seedPrograms();
        }
        if (questionRepository.count() == 0) {
            seedQuestions();
        }
    }

    private void seedPrograms() {
        studyProgramRepository.save(program(
                "Wirtschaftsinformatik",
                "Die Schnittstelle zwischen IT, Daten und betriebswirtschaftlichen Entscheidungen.",
                StudyModel.HYBRID,
                36,
                22,
                4,
                4,
                List.of("business", "data", "digitalization", "problem_solving"),
                List.of("analytics", "math", "business", "project_management"),
                List.of("hybrid", "career_switch", "leadership"),
                List.of(
                        pair("Digitale Prozesse", "Analyse und Optimierung von Geschaeftsprozessen."),
                        pair("ERP und Plattformen", "Grundlagen integrierter Unternehmenssoftware."),
                        pair("Datenanalyse", "Kennzahlen, Dashboards und datenbasierte Entscheidungen.")
                ),
                List.of(
                        pair("Business Analyst", "Uebersetzt Fachanforderungen in digitale Loesungen."),
                        pair("IT-Consultant", "Begleitet Transformationsprojekte in Unternehmen."),
                        pair("Product Owner", "Steuert Produkte an der Schnittstelle von Fachbereich und IT.")
                ),
                List.of(
                        pair("Hoher Koordinationsaufwand", "Viele Projekte erfordern Abstimmung zwischen Technik und Business."),
                        pair("Theorie in BWL und Informatik", "Die Kombination beider Welten verlangt Durchhaltevermoegen.")
                ),
                List.of("Online-Bewerbung vorbereiten", "Lebenslauf und Zeugnisse hochladen", "Motivationsschreiben fuer digitale Schnittstellenrolle verfassen", "Finanzierungsoptionen pruefen"),
                List.of(
                        routine("08:30", "Vorlesung Prozessmanagement", "Praxisnahe Fallstudien zur Digitalisierung."),
                        routine("11:00", "Workshop Datenvisualisierung", "Erste Dashboards mit echten Datensaetzen bauen."),
                        routine("14:00", "Gruppenarbeit", "Anforderungen eines Unternehmens in User Stories uebersetzen."),
                        routine("17:00", "Selbststudium", "Kurze Reflexion und Vorbereitung auf den naechsten Projekttag.")
                ),
                internationalInfo(
                        "Internationale Bewerber erhalten Unterstuetzung bei Dokumentenpruefung und Fristenplanung.",
                        "Hinweise zu Stipendien, Teilzeitjobs und Finanzierungsberatung sind verfuegbar.",
                        "Das International Office begleitet bei Visum, Aufenthalt und Ankunft.",
                        "Deutsch B2, einzelne Module auf Englisch."
                )
        ));

        studyProgramRepository.save(program(
                "Software Engineering",
                "Von Architektur ueber Clean Code bis Deployment: Software systematisch entwickeln.",
                StudyModel.FULL_TIME,
                36,
                28,
                3,
                2,
                List.of("programming", "engineering", "problem_solving", "teamwork"),
                List.of("programming", "math", "project_management", "testing"),
                List.of("full_time", "hands_on", "teamwork"),
                List.of(
                        pair("Softwarearchitektur", "Systeme modular planen und dokumentieren."),
                        pair("Web- und Cloud-Entwicklung", "Moderne Fullstack-Systeme und DevOps-Grundlagen."),
                        pair("Qualitaetssicherung", "Tests, Review-Prozesse und Wartbarkeit.")
                ),
                List.of(
                        pair("Software Engineer", "Entwickelt robuste Anwendungen fuer Unternehmen und Produkte."),
                        pair("DevOps Engineer", "Automatisiert Build-, Test- und Deployment-Prozesse."),
                        pair("Tech Lead", "Verbindet technische Exzellenz mit Teamsteuerung.")
                ),
                List.of(
                        pair("Kontinuierlicher Lernbedarf", "Frameworks und Tools entwickeln sich schnell weiter."),
                        pair("Projektintensitaet", "Semesterprojekte benoetigen konsequentes Zeitmanagement.")
                ),
                List.of("Registrierung im Bewerbungsportal", "Zeugnisse und Nachweise einreichen", "Einstufungstest pruefen", "Immatrikulationsunterlagen final absenden"),
                List.of(
                        routine("09:00", "Coding Lab", "Feature-Implementierung in Zweierteams."),
                        routine("12:00", "Code Review", "Gemeinsames Feedback zu Architektur und Tests."),
                        routine("14:30", "Architekturvorlesung", "Patterns, APIs und Systemdesign."),
                        routine("18:00", "Selbststudium", "Issues umsetzen und Sprint-Board aktualisieren.")
                ),
                internationalInfo(
                        "Bewerbungsunterlagen koennen fruehzeitig vorgeprueft werden.",
                        "Es gibt Beratung zu Scholarship-Angeboten und Wohnheimfinanzierung.",
                        "Welcome Services helfen bei Versicherung und Aufenthalt.",
                        "Englisch B2 ausreichend, Deutsch fuer Alltag empfohlen."
                )
        ));

        studyProgramRepository.save(program(
                "IT-Security",
                "Schutz digitaler Systeme, sichere Architekturen und Verteidigung gegen Angriffe.",
                StudyModel.HYBRID,
                36,
                24,
                5,
                3,
                List.of("security", "analysis", "problem_solving", "infrastructure"),
                List.of("programming", "math", "security", "analytics"),
                List.of("hybrid", "investigation", "responsibility"),
                List.of(
                        pair("Netzwerksicherheit", "Absicherung von Infrastruktur und Kommunikation."),
                        pair("Incident Response", "Angriffe erkennen, analysieren und eindammen."),
                        pair("Secure Coding", "Schwachstellen vermeiden und Reviews durchfuehren.")
                ),
                List.of(
                        pair("Security Analyst", "Ueberwacht Systeme und analysiert Sicherheitsvorfaelle."),
                        pair("Penetration Tester", "Prueft Systeme kontrolliert auf Schwachstellen."),
                        pair("Security Consultant", "Beratet Organisationen bei Schutzmassnahmen.")
                ),
                List.of(
                        pair("Hoher Theorieanteil", "Kryptografie und Sicherheitsmodelle sind anspruchsvoll."),
                        pair("Verantwortungsdruck", "Fehler koennen grosse Auswirkungen haben.")
                ),
                List.of("Bewerbungsdaten eingeben", "Technische Motivation schildern", "Identitaets- und Sprachnachweise hochladen", "Studienberatungstermin optional buchen"),
                List.of(
                        routine("08:45", "Security Monitoring", "Logdaten und Alerts analysieren."),
                        routine("11:00", "Kryptografie-Vorlesung", "Mathematische Grundlagen sicherer Kommunikation."),
                        routine("14:00", "Lab-Szenario", "Schwachstellen in Testumgebungen nachvollziehen."),
                        routine("17:30", "Nachbereitung", "Massnahmen dokumentieren und Erkenntnisse reflektieren.")
                ),
                internationalInfo(
                        "Unterlagen fuer internationale Zeugnisanerkennung werden begleitet.",
                        "Beratung zu Foerderprogrammen und Notfallfonds ist vorhanden.",
                        "Unterstuetzung bei Visa- und Meldeprozessen steht bereit.",
                        "Deutsch oder Englisch B2, technische Fachbegriffe werden begleitet."
                )
        ));

        studyProgramRepository.save(program(
                "Data Science",
                "Daten verstehen, Modelle entwickeln und Erkenntnisse in Entscheidungen uebersetzen.",
                StudyModel.FULL_TIME,
                36,
                27,
                5,
                2,
                List.of("data", "research", "math", "analysis"),
                List.of("math", "analytics", "programming", "research"),
                List.of("full_time", "research", "deep_work"),
                List.of(
                        pair("Machine Learning", "Modelle trainieren und evaluieren."),
                        pair("Statistik", "Unsicherheiten verstehen und Experimente auswerten."),
                        pair("Data Engineering Basics", "Saubere Datengrundlagen fuer Analysen schaffen.")
                ),
                List.of(
                        pair("Data Scientist", "Entwickelt Modelle und kommuniziert Erkenntnisse."),
                        pair("BI Analyst", "Bereitet Daten fuer Management-Entscheidungen auf."),
                        pair("ML Engineer", "Bringt Modelle in produktive Systeme.")
                ),
                List.of(
                        pair("Mathematische Anforderungen", "Statistik und lineare Algebra sind zentral."),
                        pair("Hoher Selbstlernanteil", "Methoden muessen kontinuierlich vertieft werden.")
                ),
                List.of("Online-Formular ausfuellen", "Transcripts und Lebenslauf hochladen", "Mathe-Selbsteinschaetzung vorbereiten", "Eignungsgespraech optional absolvieren"),
                List.of(
                        routine("09:00", "Statistik-Vorlesung", "Modelle und Wahrscheinlichkeiten diskutieren."),
                        routine("11:30", "Notebook-Lab", "Datensaetze bereinigen und Modelle trainieren."),
                        routine("15:00", "Projektarbeit", "Business-Frage in datengetriebene Hypothesen uebersetzen."),
                        routine("18:00", "Selbststudium", "Visualisierung und Dokumentation verfeinern.")
                ),
                internationalInfo(
                        "Internationale Bewerber bekommen Checklisten fuer Zeugnisbewertung.",
                        "Infos zu Scholarships und Living Costs werden bereitgestellt.",
                        "Residence-Support hilft bei Behoerdengaengen und Ankunft.",
                        "Englisch B2 bis C1 empfohlen, deutschsprachige Services optional."
                )
        ));

        studyProgramRepository.save(program(
                "Digital Business",
                "Digitale Geschaeftsmodelle, Plattformstrategien und Innovation in Teams gestalten.",
                StudyModel.PART_TIME,
                30,
                18,
                2,
                5,
                List.of("business", "marketing", "digitalization", "leadership"),
                List.of("business", "communication", "project_management", "analytics"),
                List.of("part_time", "hybrid", "leadership", "career_switch"),
                List.of(
                        pair("Digitale Geschaeftsmodelle", "Produkte, Plattformen und Innovation bewerten."),
                        pair("Customer Experience", "Nutzerzentrierte Services gestalten."),
                        pair("Transformation Leadership", "Teams durch Veraenderungen fuehren.")
                ),
                List.of(
                        pair("Digital Product Manager", "Verantwortet digitale Angebote end-to-end."),
                        pair("Innovation Manager", "Entwickelt neue Geschaeftsmodelle."),
                        pair("E-Commerce Lead", "Steuert digitale Vertriebskanaele und Wachstumsinitiativen.")
                ),
                List.of(
                        pair("Viele Praesentationen", "Kommunikation und Moderation sind zentrale Bestandteile."),
                        pair("Eigenorganisation neben Beruf", "Berufsbegleitendes Lernen braucht Struktur.")
                ),
                List.of("Bewerbungsportal ausfuellen", "CV und Motivationsschreiben hochladen", "Berufserfahrung dokumentieren", "Finanzierung und Zeitplanung abstimmen"),
                List.of(
                        routine("18:00", "Abendvorlesung", "Digitale Strategien anhand realer Cases analysieren."),
                        routine("19:30", "Peer-Diskussion", "Erfahrungen aus unterschiedlichen Branchen vergleichen."),
                        routine("20:30", "Mini-Workshop", "Roadmaps und Pitch-Decks ausarbeiten."),
                        routine("21:15", "Transferphase", "Lerninhalte direkt auf den eigenen Berufsalltag anwenden.")
                ),
                internationalInfo(
                        "Remote-Onboarding und Bewerbungsberatung sind verfuegbar.",
                        "Foerderberatung fuer Berufstaetige und internationale Lernende ist moeglich.",
                        "Unterstuetzung bei Aufenthaltstiteln fuer berufsbegleitende Formate wird angeboten.",
                        "Englisch B2, deutschsprachige Kommunikationsbereitschaft hilfreich."
                )
        ));

        studyProgramRepository.save(program(
                "Projektmanagement & IT",
                "IT-Projekte sicher planen, steuern und mit Teams erfolgreich umsetzen.",
                StudyModel.PART_TIME,
                24,
                16,
                3,
                5,
                List.of("leadership", "teamwork", "business", "problem_solving"),
                List.of("project_management", "communication", "business", "analytics"),
                List.of("part_time", "hybrid", "leadership", "career_switch"),
                List.of(
                        pair("Agiles Projektmanagement", "Scrum, Kanban und klassische Methoden kombinieren."),
                        pair("Stakeholder-Kommunikation", "Anforderungen, Konflikte und Ziele transparent steuern."),
                        pair("IT-Governance", "Prozesse, Risiken und Projektkennzahlen im Blick behalten.")
                ),
                List.of(
                        pair("IT Project Manager", "Fuehrt Teams, Budgets und Releases."),
                        pair("PMO Specialist", "Schafft Standards und Transparenz fuer Projektportfolios."),
                        pair("Delivery Manager", "Sichert Wertbeitrag und Zusammenarbeit ueber Bereiche hinweg.")
                ),
                List.of(
                        pair("Viele Parallelaufgaben", "Kommunikation, Termine und Delivery laufen gleichzeitig."),
                        pair("Verantwortung fuer Teams", "Konflikte und Priorisierung gehoeren zum Alltag.")
                ),
                List.of("Formular absenden", "Berufserfahrung angeben", "Motivationsschreiben zu Fuehrungsinteresse beilegen", "Zulassung final bestaetigen"),
                List.of(
                        routine("17:30", "Projektstatus-Meeting", "Fortschritte, Risiken und Prioritaeten abstimmen."),
                        routine("18:30", "Vorlesung Agile Delivery", "Praxisbeispiele fuer Sprint- und Releaseplanung."),
                        routine("20:00", "Case Study", "Kommunikations- und Eskalationsszenarien loesen."),
                        routine("21:00", "Transfer in den Beruf", "Methoden direkt auf aktuelle Projekte anwenden.")
                ),
                internationalInfo(
                        "Internationales Bewerbungscoaching und Dokumentensupport sind verfuegbar.",
                        "Es gibt Beratung zu Foerderungen fuer berufsbegleitende Programme.",
                        "Unterstuetzung bei Aufenthalt, Arbeitsrecht und Studienorganisation ist vorhanden.",
                        "Deutsch B2 empfohlen, einzelne Inhalte bilingual."
                )
        ));
    }

    private StudyProgram program(
            String name,
            String shortDescription,
            StudyModel studyModel,
            int durationMonths,
            int workload,
            int theoryLevel,
            int flexibilityLevel,
            List<String> interests,
            List<String> skills,
            List<String> preferences,
            List<ItemPair> contents,
            List<ItemPair> careers,
            List<ItemPair> risks,
            List<String> checklist,
            List<RoutineSeed> routine,
            InternationalSeed internationalSeed
    ) {
        StudyProgram program = new StudyProgram();
        program.setName(name);
        program.setShortDescription(shortDescription);
        program.setStudyModel(studyModel);
        program.setDurationMonths(durationMonths);
        program.setEstimatedWorkloadHoursPerWeek(workload);
        program.setTheoryLevel(theoryLevel);
        program.setFlexibilityLevel(flexibilityLevel);
        program.setInterestTags(interests);
        program.setSkillTags(skills);
        program.setPreferenceTags(preferences);

        for (int index = 0; index < contents.size(); index++) {
            StudyProgramContent content = new StudyProgramContent();
            content.setStudyProgram(program);
            content.setSortOrder(index + 1);
            content.setTitle(contents.get(index).title());
            content.setDescription(contents.get(index).description());
            program.getContents().add(content);
        }
        for (int index = 0; index < careers.size(); index++) {
            CareerPath careerPath = new CareerPath();
            careerPath.setStudyProgram(program);
            careerPath.setSortOrder(index + 1);
            careerPath.setTitle(careers.get(index).title());
            careerPath.setDescription(careers.get(index).description());
            program.getCareerPaths().add(careerPath);
        }
        for (int index = 0; index < risks.size(); index++) {
            RiskItem riskItem = new RiskItem();
            riskItem.setStudyProgram(program);
            riskItem.setSortOrder(index + 1);
            riskItem.setTitle(risks.get(index).title());
            riskItem.setDescription(risks.get(index).description());
            program.getRisks().add(riskItem);
        }
        for (int index = 0; index < checklist.size(); index++) {
            ApplicationChecklistItem item = new ApplicationChecklistItem();
            item.setStudyProgram(program);
            item.setSortOrder(index + 1);
            item.setItem(checklist.get(index));
            program.getApplicationChecklistItems().add(item);
        }
        for (int index = 0; index < routine.size(); index++) {
            DailyRoutineEntry entry = new DailyRoutineEntry();
            entry.setStudyProgram(program);
            entry.setSortOrder(index + 1);
            entry.setTimeSlot(routine.get(index).timeSlot());
            entry.setTitle(routine.get(index).title());
            entry.setDescription(routine.get(index).description());
            program.getDailyRoutineEntries().add(entry);
        }

        InternationalSupportInfo info = new InternationalSupportInfo();
        info.setStudyProgram(program);
        info.setApplicationInfo(internationalSeed.applicationInfo());
        info.setFinancingInfo(internationalSeed.financingInfo());
        info.setResidenceInfo(internationalSeed.residenceInfo());
        info.setLanguageRequirements(internationalSeed.languageRequirements());
        program.setInternationalSupportInfo(info);

        return program;
    }

    private void seedQuestions() {
        List<Question> questions = List.of(
                question(1, "interest_topics", "Welche Themen interessieren dich am meisten?", "Waehle bis zu drei Themen, die dich spontan anziehen.", QuestionType.MULTIPLE_CHOICE, List.of(
                        "programming", "data", "security", "business", "digitalization", "leadership", "marketing", "problem_solving"
                )),
                question(2, "interest_tasks", "Welche Aufgaben machen dir am meisten Spass?", "Mehrfachauswahl moeglich.", QuestionType.MULTIPLE_CHOICE, List.of(
                        "analysis", "engineering", "teamwork", "research", "communication", "investigation"
                )),
                question(3, "existing_skills", "Welche Skills bringst du schon mit?", "Waehl alles aus, was heute schon auf dich zutrifft.", QuestionType.MULTIPLE_CHOICE, List.of(
                        "programming", "business", "math", "project_management", "analytics", "communication", "security", "testing"
                )),
                question(4, "programming_confidence", "Wie sicher fuehlst du dich beim Programmieren?", "1 = noch gar nicht, 5 = sehr sicher.", QuestionType.SCALE, List.of()),
                question(5, "math_confidence", "Wie wohl fuehlst du dich mit Mathematik und Statistik?", "1 = eher unsicher, 5 = macht mir Spass.", QuestionType.SCALE, List.of()),
                question(6, "work_experience", "Wie viel Berufserfahrung bringst du mit?", "Hilft bei der Einordnung berufsbegleitender Programme.", QuestionType.SCALE, List.of()),
                question(7, "study_model", "Welches Studienmodell passt am besten in dein Leben?", "Single Choice fuer deinen bevorzugten Rahmen.", QuestionType.SINGLE_CHOICE, List.of(
                        "full_time", "part_time", "hybrid"
                )),
                question(8, "theory_vs_practice", "Wie theoretisch darf dein Studium sein?", "1 = sehr praxisnah, 5 = gerne tief theoretisch.", QuestionType.SCALE, List.of()),
                question(9, "learning_style", "Wie lernst du am liebsten?", "Waehl die Formate, die dich motivieren.", QuestionType.MULTIPLE_CHOICE, List.of(
                        "hands_on", "teamwork", "deep_work", "research", "hybrid"
                )),
                question(10, "career_goal", "Welche berufliche Richtung reizt dich aktuell?", "Mehrfachauswahl moeglich.", QuestionType.MULTIPLE_CHOICE, List.of(
                        "leadership", "career_switch", "research", "consulting", "product", "security"
                )),
                question(11, "international_support", "Ist internationale Unterstuetzung fuer dich wichtig?", "Mehrfachauswahl, falls du aus dem Ausland kommst oder internationale Optionen suchst.", QuestionType.MULTIPLE_CHOICE, List.of(
                        "visa_support", "english_modules", "funding_support", "global_network"
                ))
        );
        questionRepository.saveAll(questions);
    }

    private Question question(int order, String key, String title, String description, QuestionType type, List<String> options) {
        Question question = new Question();
        question.setSortOrder(order);
        question.setQuestionKey(key);
        question.setTitle(title);
        question.setDescription(description);
        question.setType(type);
        question.setRequiredQuestion(true);
        if (type == QuestionType.SCALE) {
            question.setMinScale(1);
            question.setMaxScale(5);
        }
        for (int index = 0; index < options.size(); index++) {
            QuestionOption option = new QuestionOption();
            option.setQuestion(question);
            option.setSortOrder(index + 1);
            option.setOptionValue(options.get(index));
            option.setLabel(humanize(options.get(index)));
            question.getOptions().add(option);
        }
        return question;
    }

    private String humanize(String value) {
        return value.replace("_", " ");
    }

    private ItemPair pair(String title, String description) {
        return new ItemPair(title, description);
    }

    private RoutineSeed routine(String timeSlot, String title, String description) {
        return new RoutineSeed(timeSlot, title, description);
    }

    private InternationalSeed internationalInfo(String applicationInfo, String financingInfo, String residenceInfo, String languageRequirements) {
        return new InternationalSeed(applicationInfo, financingInfo, residenceInfo, languageRequirements);
    }

    private record ItemPair(String title, String description) {
    }

    private record RoutineSeed(String timeSlot, String title, String description) {
    }

    private record InternationalSeed(String applicationInfo, String financingInfo, String residenceInfo, String languageRequirements) {
    }
}

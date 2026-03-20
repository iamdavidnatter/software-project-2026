# Studiengangs-Finder

Eine mobile-first Fullstack-Webanwendung fuer eine Hochschulmesse. Besucher scannen einen QR-Code, beantworten einen strukturierten Fragebogen und erhalten passende Studienempfehlungen mit Matching-Scores, Vergleich, Favoriten, Risikoanalyse, Karriereperspektiven, PDF-Zusammenfassung und persoenlichem Ergebnis-Link.

## Projektstruktur

```text
software-project-2026/
├── backend/
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/
│       ├── java/com/finder/studiengangfinder/
│       │   ├── config/
│       │   ├── controller/
│       │   ├── dto/
│       │   ├── entity/
│       │   ├── enums/
│       │   ├── exception/
│       │   ├── repository/
│       │   ├── seed/
│       │   └── service/
│       └── resources/application.yml
├── frontend/
│   ├── package.json
│   ├── Dockerfile
│   ├── index.html
│   ├── vite.config.ts
│   └── src/
│       ├── api/
│       ├── components/
│       ├── pages/
│       ├── styles/
│       ├── types/
│       └── utils/
├── docker-compose.yml
└── .env.example
```

## Features

- Wizard-basierter Fragebogen mit 11 Fragen, Fortschrittsanzeige und mobile-first UX
- Matching-Engine auf Basis von Interessen, Skills, Erfahrung, Studienmodell und Zeitbudget
- Ergebnisseite mit Top-Empfehlung, Matching-Scores, Risikoanalyse und Karriereperspektiven
- Studiengang-Vergleich fuer mehrere Programme
- Persistente Favoriten pro Ergebnis-Token
- Typischer Studienalltag, internationale Unterstuetzung und Bewerbungs-Checklisten
- PDF-Export im Spring-Boot-Backend
- Persoenlicher Ergebnis-Link auf UUID-Basis
- Automatisches Seedings fuer Beispiel-Studiengaenge und Fragen

## Verwendete Technologien

- Backend: Java 21, Spring Boot, Spring Data JPA, Hibernate, Maven
- Frontend: React 18, TypeScript, Vite
- Datenbank: PostgreSQL 16
- PDF: OpenPDF
- Container: Docker Compose

## Voraussetzungen

- Java 21
- Maven 3.9+
- Node.js 20+
- npm 10+
- PostgreSQL 16

## Lokales Setup ohne Docker

### 1. PostgreSQL starten

Lege eine Datenbank `studiengang_finder` an und verwende beispielsweise:

- Benutzer: `finder`
- Passwort: `finder`

### 2. Umgebungsvariablen setzen

Nutze `.env.example` als Vorlage. Wichtige Werte:

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/studiengang_finder
export SPRING_DATASOURCE_USERNAME=finder
export SPRING_DATASOURCE_PASSWORD=finder
export APP_FRONTEND_BASE_URL=http://localhost:5173
export VITE_API_BASE_URL=http://localhost:8080/api
```

### 3. Backend starten

```bash
cd backend
mvn spring-boot:run
```

Das Backend startet standardmaessig auf [http://localhost:8080](http://localhost:8080).

### 4. Frontend starten

```bash
cd frontend
npm install
npm run dev
```

Das Frontend laeuft standardmaessig auf [http://localhost:5173](http://localhost:5173).

## Start mit Docker Compose

```bash
docker compose up --build
```

Danach:

- Frontend: [http://localhost:5173](http://localhost:5173)
- Backend: [http://localhost:8080](http://localhost:8080)
- PostgreSQL: `localhost:5432`

## Seed-Daten

Beim ersten Start werden automatisch Beispiel-Studiengaenge erzeugt:

- Wirtschaftsinformatik
- Software Engineering
- IT-Security
- Data Science
- Digital Business
- Projektmanagement & IT

Zusaetzlich werden 11 Fragen fuer den Fragebogen initialisiert.

## API-Ueberblick

- `GET /api/questionnaire`
- `POST /api/sessions`
- `POST /api/sessions/{sessionId}/answers`
- `GET /api/study-programs`
- `POST /api/study-programs/compare`
- `GET /api/results/{token}`
- `PUT /api/results/{token}/favorites`
- `GET /api/results/{token}/pdf`

## Matching-Logik

Die Matching-Engine kombiniert mehrere Teilwerte:

- Interessen-Overlap mit Studiengang-Tags
- Skills-Overlap mit empfohlenen Vorkenntnissen
- Studienpraeferenzen und Studienmodell
- Theorie-/Praxis-Fit
- Berufserfahrung
- Verfuegbares Wochen-Zeitbudget

Die Scores werden als Prozentwerte ausgegeben und bewusst leicht nachvollziehbar gehalten, damit die Logik spaeter einfach erweitert oder verfeinert werden kann.

## Hinweise

- Das Datenbankschema wird ueber Hibernate automatisch erzeugt.
- Favoriten und Ergebnisse sind an die Session bzw. den Ergebnis-Token gebunden.
- PDFs werden serverseitig erzeugt und enthalten Ergebnis-Link, Top-Empfehlungen und Profilzusammenfassung.

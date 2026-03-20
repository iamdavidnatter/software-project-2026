import { useEffect, useMemo, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { api } from "../api/client";
import ComparePanel from "../components/ComparePanel";
import ProgramCard from "../components/ProgramCard";
import { formatModel, titleizeTag } from "../utils/format";
import type { Result, StudyProgram } from "../types/api";

export default function ResultsPage() {
  const { token } = useParams();
  const [result, setResult] = useState<Result | null>(null);
  const [compareIds, setCompareIds] = useState<number[]>([]);
  const [comparePrograms, setComparePrograms] = useState<StudyProgram[]>([]);
  const [modelFilter, setModelFilter] = useState("ALL");
  const [error, setError] = useState("");
  const [copyFeedback, setCopyFeedback] = useState("");
  const [copyUrl, setCopyUrl] = useState("");

  useEffect(() => {
    if (!token) {
      return;
    }
    api.getResult(token)
      .then(setResult)
      .catch((err) => setError(err instanceof Error ? err.message : "Ergebnis konnte nicht geladen werden."));
  }, [token]);

  useEffect(() => {
    if (compareIds.length < 2) {
      setComparePrograms([]);
      return;
    }
    api.comparePrograms(compareIds)
      .then(setComparePrograms)
      .catch((err) => setError(err instanceof Error ? err.message : "Vergleich konnte nicht geladen werden."));
  }, [compareIds]);

  const filteredRecommendations = useMemo(() => {
    if (!result) {
      return [];
    }
    return result.recommendations.filter((item) => modelFilter === "ALL" || item.details.studyModel === modelFilter);
  }, [modelFilter, result]);

  const favoriteRecommendations = useMemo(() => {
    if (!result) {
      return [];
    }
    return result.recommendations.filter((item) => result.favoriteProgramIds.includes(item.programId));
  }, [result]);

  const toggleFavorite = async (programId: number) => {
    if (!result || !token) {
      return;
    }
    const current = new Set(result.favoriteProgramIds);
    if (current.has(programId)) {
      current.delete(programId);
    } else {
      current.add(programId);
    }
    const favoriteProgramIds = Array.from(current);
    await api.updateFavorites(token, favoriteProgramIds);
    setResult({ ...result, favoriteProgramIds });
  };

  const toggleCompare = (programId: number) => {
    setCompareIds((previous) => {
      if (previous.includes(programId)) {
        return previous.filter((item) => item !== programId);
      }
      return previous.length >= 3 ? [...previous.slice(1), programId] : [...previous, programId];
    });
  };

  const handlePdfDownload = async () => {
    if (!token) {
      return;
    }
    const blob = await api.downloadPdf(token);
    const url = URL.createObjectURL(blob);
    const link = document.createElement("a");
    link.href = url;
    link.download = "studiengangs-finder-ergebnis.pdf";
    link.click();
    URL.revokeObjectURL(url);
  };

  const handleCopyLink = async () => {
    if (!token) {
      return;
    }
    const url = `${window.location.origin}/results/${token}`;
    setCopyUrl(url);
    try {
      if (navigator.clipboard?.writeText) {
        await navigator.clipboard.writeText(url);
      } else {
        const textarea = document.createElement("textarea");
        textarea.value = url;
        textarea.setAttribute("readonly", "true");
        textarea.style.position = "absolute";
        textarea.style.left = "-9999px";
        document.body.appendChild(textarea);
        textarea.select();
        document.execCommand("copy");
        document.body.removeChild(textarea);
      }
      setCopyFeedback("Link kopiert");
    } catch {
      setCopyFeedback("Link unten manuell kopieren");
    }
    window.setTimeout(() => setCopyFeedback(""), 2200);
  };

  if (!result) {
    return (
      <main className="page-shell narrow-shell">
        <section className="card">
          <h1>Ergebnis wird geladen</h1>
          <p>{error || "Bitte einen Moment Geduld."}</p>
        </section>
      </main>
    );
  }

  return (
    <main className="page-shell results-shell">
      <section className="hero-result">
        <div className="card spotlight-card">
          <span className="eyebrow">Top-Empfehlung</span>
          <h1>{result.topRecommendation?.programName}</h1>
          <div className="score-hero">{result.topRecommendation?.score}% Match</div>
          <p>{result.topRecommendation?.rationale}</p>
          <div className="tag-row">
            {result.selectedInterests.map((tag) => (
              <span className="tag" key={tag}>{titleizeTag(tag)}</span>
            ))}
          </div>
          <div className="action-row">
            <button type="button" className="primary-button" onClick={handlePdfDownload}>
              PDF herunterladen
            </button>
            <button type="button" className="ghost-button" onClick={handleCopyLink}>
              Persoenlichen Link kopieren
            </button>
            <Link to="/" className="ghost-button">
              Test neu starten
            </Link>
          </div>
          {copyUrl ? <input readOnly value={copyUrl} aria-label="Persoenlicher Ergebnis-Link" /> : null}
          {copyFeedback ? <p className="success-text">{copyFeedback}</p> : null}
        </div>

        <aside className="card summary-card">
          <span className="eyebrow">Profil</span>
          <h2>{result.nickname}</h2>
          <p>{result.profileSummary}</p>
          <div className="summary-list">
            <div>
              <h4>Skills</h4>
              <p>{result.selectedSkills.map(titleizeTag).join(", ") || "Noch wenige Vorkenntnisse"}</p>
            </div>
            <div>
              <h4>Zeitbudget</h4>
              <p>{result.availableHoursPerWeek} Std. pro Woche</p>
            </div>
            <div>
              <h4>Favoriten</h4>
              <p>{result.favoriteProgramIds.length} gespeichert</p>
            </div>
          </div>
        </aside>
      </section>

      <section className="toolbar">
        <div>
          <span className="eyebrow">Filter</span>
          <h2>Passende Studienmodelle</h2>
        </div>
        <div className="filter-row">
          {["ALL", "FULL_TIME", "PART_TIME", "HYBRID"].map((filterValue) => (
            <button
              key={filterValue}
              type="button"
              className={`ghost-button ${modelFilter === filterValue ? "active" : ""}`}
              onClick={() => setModelFilter(filterValue)}
            >
              {filterValue === "ALL" ? "Alle" : formatModel(filterValue)}
            </button>
          ))}
        </div>
      </section>

      <section className="result-grid">
        {filteredRecommendations.map((recommendation) => (
          <ProgramCard
            key={recommendation.programId}
            recommendation={{
              ...recommendation,
              favorite: result.favoriteProgramIds.includes(recommendation.programId),
            }}
            selectedForCompare={compareIds.includes(recommendation.programId)}
            onToggleFavorite={toggleFavorite}
            onToggleCompare={toggleCompare}
          />
        ))}
      </section>

      <ComparePanel programs={comparePrograms} />

      <section className="card detail-card">
        <div className="section-header">
          <div>
            <span className="eyebrow">Favoriten</span>
            <h2>Gemerkte Studiengaenge</h2>
          </div>
          <p>{favoriteRecommendations.length === 0 ? "Noch nichts gespeichert" : `${favoriteRecommendations.length} Programme gespeichert`}</p>
        </div>
        {favoriteRecommendations.length === 0 ? (
          <p>Markiere interessante Programme als Favorit. So kannst du sie gesammelt vergleichen, spaeter per Link wiederfinden und im PDF leichter nachverfolgen.</p>
        ) : (
          <div className="stack-list">
            {favoriteRecommendations.map((item) => (
              <div className="stack-item" key={item.programId}>
                <div>
                  <h4>{item.programName}</h4>
                  <p>{item.details.shortDescription}</p>
                </div>
                <div className="stack-metrics">
                  <strong>{item.score}%</strong>
                  <span>{item.details.careerPaths[0]?.title ?? "Karrierepfad"}</span>
                </div>
              </div>
            ))}
          </div>
        )}
      </section>

      <section className="details-grid">
        <article className="card detail-card">
          <span className="eyebrow">Studienalltag</span>
          <h2>Typischer Tag in {result.topRecommendation?.programName}</h2>
          <div className="timeline">
            {result.topRecommendation?.details.dailyRoutine.map((entry) => (
              <div key={entry.timeSlot} className="timeline-item">
                <strong>{entry.timeSlot}</strong>
                <div>
                  <h4>{entry.title}</h4>
                  <p>{entry.description}</p>
                </div>
              </div>
            ))}
          </div>
        </article>

        <article className="card detail-card">
          <span className="eyebrow">International</span>
          <h2>Unterstuetzung fuer internationale Studierende</h2>
          <div className="summary-list">
            <div>
              <h4>Bewerbung</h4>
              <p>{result.topRecommendation?.details.internationalSupport.applicationInfo}</p>
            </div>
            <div>
              <h4>Finanzierung</h4>
              <p>{result.topRecommendation?.details.internationalSupport.financingInfo}</p>
            </div>
            <div>
              <h4>Aufenthalt</h4>
              <p>{result.topRecommendation?.details.internationalSupport.residenceInfo}</p>
            </div>
            <div>
              <h4>Sprache</h4>
              <p>{result.topRecommendation?.details.internationalSupport.languageRequirements}</p>
            </div>
          </div>
        </article>

        <article className="card detail-card">
          <span className="eyebrow">Bewerbungs-Checkliste</span>
          <h2>Naechste Schritte</h2>
          <ul className="checklist">
            {result.topRecommendation?.details.applicationChecklist.map((item) => (
              <li key={item}>{item}</li>
            ))}
          </ul>
        </article>
      </section>

      {error ? <p className="error-text">{error}</p> : null}
    </main>
  );
}

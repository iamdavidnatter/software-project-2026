import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { api } from "../api/client";
import { formatModel } from "../utils/format";
import type { AdminDashboard } from "../types/api";

export default function AdminDashboardPage() {
  const [dashboard, setDashboard] = useState<AdminDashboard | null>(null);
  const [error, setError] = useState("");

  useEffect(() => {
    api.getAdminDashboard()
      .then(setDashboard)
      .catch((err) => setError(err instanceof Error ? err.message : "Dashboard konnte nicht geladen werden."));
  }, []);

  if (!dashboard) {
    return (
      <main className="page-shell narrow-shell">
        <section className="card">
          <h1>Admin-Dashboard wird geladen</h1>
          <p>{error || "Aggregierte Messe-Daten werden vorbereitet."}</p>
        </section>
      </main>
    );
  }

  return (
    <main className="page-shell results-shell">
      <section className="toolbar">
        <div>
          <span className="eyebrow">Admin Dashboard</span>
          <h1>Messe-Ergebnisse im Ueberblick</h1>
          <p>Zusammenfassung der bisher eingegangenen Tests und beliebtesten Studiengaenge.</p>
        </div>
        <div className="action-row">
          <Link className="ghost-button" to="/">Zur Startseite</Link>
        </div>
      </section>

      <section className="result-grid">
        <article className="card">
          <span className="eyebrow">Sessions</span>
          <h2>{dashboard.totalSessions}</h2>
          <p>Gestartete Sessions insgesamt</p>
        </article>
        <article className="card">
          <span className="eyebrow">Abgeschlossen</span>
          <h2>{dashboard.completedResults}</h2>
          <p>Berechnete Ergebnisse</p>
        </article>
        <article className="card">
          <span className="eyebrow">Favoriten</span>
          <h2>{dashboard.totalFavorites}</h2>
          <p>Gespeicherte Studiengaenge</p>
        </article>
        <article className="card">
          <span className="eyebrow">Beliebtestes Modell</span>
          <h2>{formatModel(dashboard.mostPopularStudyModel)}</h2>
          <p>Hauefigstes Top-Ergebnis</p>
        </article>
      </section>

      <section className="details-grid admin-grid">
        <article className="card detail-card">
          <span className="eyebrow">Top Programme</span>
          <h2>Beliebteste Empfehlungen</h2>
          <div className="stack-list">
            {dashboard.topPrograms.map((program) => (
              <div className="stack-item" key={program.programId}>
                <div>
                  <h4>{program.programName}</h4>
                  <p>{program.topRecommendationCount} mal Top-Empfehlung</p>
                </div>
                <div className="stack-metrics">
                  <strong>{program.averageScore.toFixed(0)}%</strong>
                  <span>{program.favoriteCount} Favoriten</span>
                </div>
              </div>
            ))}
          </div>
        </article>

        <article className="card detail-card">
          <span className="eyebrow">Letzte Ergebnisse</span>
          <h2>Letzte Messebesucher</h2>
          <div className="stack-list">
            {dashboard.recentResults.map((entry) => (
              <Link key={entry.token} className="stack-item link-item" to={`/results/${entry.token}`}>
                <div>
                  <h4>{entry.nickname}</h4>
                  <p>{entry.topProgramName} bei {entry.topScore}% Match</p>
                </div>
                <div className="stack-metrics">
                  <strong>{entry.availableHoursPerWeek} Std.</strong>
                  <span>{new Date(entry.generatedAt).toLocaleString("de-DE")}</span>
                </div>
              </Link>
            ))}
          </div>
        </article>
      </section>

      {error ? <p className="error-text">{error}</p> : null}
    </main>
  );
}

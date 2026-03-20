import { FormEvent, useState } from "react";
import { useNavigate } from "react-router-dom";
import { api } from "../api/client";

export default function LandingPage() {
  const navigate = useNavigate();
  const [nickname, setNickname] = useState("");
  const [hours, setHours] = useState(15);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setLoading(true);
    setError("");
    try {
      const session = await api.createSession(nickname, hours);
      navigate(`/questionnaire/${session.sessionId}`, {
        state: {
          nickname: session.nickname,
          resultToken: session.resultToken,
        },
      });
    } catch (err) {
      setError(err instanceof Error ? err.message : "Session konnte nicht erstellt werden.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <main className="page-shell landing-shell">
      <section className="hero-panel">
        <div className="hero-copy">
          <span className="eyebrow">Hochschulmesse 2026</span>
          <h1>Studiengangs-Finder</h1>
          <p>
            Scanne den QR-Code, beantworte den mobilen Fragebogen und erhalte
            sofort passende Studienempfehlungen mit Score, Risiken, Karrierewegen,
            PDF-Download und persoenlichem Ergebnis-Link.
          </p>
          <div className="hero-points">
            <span>Mobile-first</span>
            <span>Vergleich & Favoriten</span>
            <span>PDF & Ergebnis-Link</span>
          </div>
        </div>

        <form className="card hero-form" onSubmit={handleSubmit}>
          <span className="eyebrow">Loslegen</span>
          <h2>Dein Einstieg</h2>
          <label>
            Nickname
            <input
              placeholder="z. B. Alex"
              value={nickname}
              onChange={(event) => setNickname(event.target.value)}
            />
          </label>
          <label>
            Verfuegbare Stunden pro Woche
            <input
              type="range"
              min={0}
              max={40}
              value={hours}
              onChange={(event) => setHours(Number(event.target.value))}
            />
            <strong>{hours} Std. pro Woche</strong>
          </label>
          <button type="submit" className="primary-button" disabled={loading}>
            {loading ? "Session wird erstellt..." : "Fragebogen starten"}
          </button>
          {error ? <p className="error-text">{error}</p> : null}
        </form>
      </section>
    </main>
  );
}

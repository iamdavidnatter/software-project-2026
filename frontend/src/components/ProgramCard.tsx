import { formatModel, titleizeTag } from "../utils/format";
import type { Recommendation } from "../types/api";

type ProgramCardProps = {
  recommendation: Recommendation;
  selectedForCompare: boolean;
  onToggleFavorite: (programId: number) => void;
  onToggleCompare: (programId: number) => void;
};

export default function ProgramCard({
  recommendation,
  selectedForCompare,
  onToggleFavorite,
  onToggleCompare,
}: ProgramCardProps) {
  return (
    <article className={`card result-card ${recommendation.score >= 80 ? "top" : ""}`}>
      <div className="result-head">
        <div>
          <span className="eyebrow">{formatModel(recommendation.details.studyModel)}</span>
          <h3>{recommendation.programName}</h3>
        </div>
        <div className="score-badge">{recommendation.score}%</div>
      </div>

      <p className="muted">{recommendation.details.shortDescription}</p>

      <div className="tag-row">
        {recommendation.details.skillTags.slice(0, 4).map((tag) => (
          <span className="tag" key={tag}>{titleizeTag(tag)}</span>
        ))}
      </div>

      <div className="mini-stats">
        <div>
          <span>Aufwand</span>
          <strong>{recommendation.estimatedWorkloadHoursPerWeek} Std.</strong>
        </div>
        <div>
          <span>Dauer</span>
          <strong>{recommendation.details.durationMonths} Monate</strong>
        </div>
        <div>
          <span>Theorie</span>
          <strong>{recommendation.details.theoryLevel}/5</strong>
        </div>
      </div>

      <p>{recommendation.rationale}</p>

      <div className="section-list">
        <div>
          <h4>Karriereperspektiven</h4>
          <ul>
            {recommendation.details.careerPaths.slice(0, 3).map((career) => (
              <li key={career.title}>{career.title}</li>
            ))}
          </ul>
        </div>
        <div>
          <h4>Risikoanalyse</h4>
          <p>{recommendation.dynamicRiskSummary}</p>
        </div>
      </div>

      <div className="action-row">
        <button type="button" className="ghost-button" onClick={() => onToggleFavorite(recommendation.programId)}>
          {recommendation.favorite ? "Favorit entfernt" : "Zu Favoriten"}
        </button>
        <button
          type="button"
          className={`ghost-button ${selectedForCompare ? "active" : ""}`}
          onClick={() => onToggleCompare(recommendation.programId)}
        >
          {selectedForCompare ? "Im Vergleich" : "Vergleichen"}
        </button>
      </div>
    </article>
  );
}

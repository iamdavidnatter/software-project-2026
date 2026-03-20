import { formatModel } from "../utils/format";
import type { StudyProgram } from "../types/api";

type ComparePanelProps = {
  programs: StudyProgram[];
};

export default function ComparePanel({ programs }: ComparePanelProps) {
  if (programs.length === 0) {
    return null;
  }

  return (
    <section className="card compare-panel">
      <div className="section-header">
        <div>
          <span className="eyebrow">Direktvergleich</span>
          <h2>Studiengaenge nebeneinander</h2>
        </div>
      </div>
      <div className="compare-grid">
        {programs.map((program) => (
          <article key={program.id} className="compare-card">
            <h3>{program.name}</h3>
            <p>{program.shortDescription}</p>
            <dl>
              <dt>Studienmodell</dt>
              <dd>{formatModel(program.studyModel)}</dd>
              <dt>Dauer</dt>
              <dd>{program.durationMonths} Monate</dd>
              <dt>Karrierechancen</dt>
              <dd>{program.careerPaths.map((item) => item.title).join(", ")}</dd>
              <dt>Inhalte</dt>
              <dd>{program.contents.map((item) => item.title).join(", ")}</dd>
            </dl>
          </article>
        ))}
      </div>
    </section>
  );
}

import type { Question } from "../types/api";

type QuestionCardProps = {
  question: Question;
  value: string[];
  scaleValue?: number;
  onSelect: (next: string[]) => void;
  onScale: (next: number) => void;
};

export default function QuestionCard({
  question,
  value,
  scaleValue,
  onSelect,
  onScale,
}: QuestionCardProps) {
  if (question.type === "SCALE") {
    const values = Array.from(
      { length: (question.maxScale ?? 5) - (question.minScale ?? 1) + 1 },
      (_, index) => (question.minScale ?? 1) + index,
    );

    return (
      <section className="card question-card">
        <span className="eyebrow">Selbsteinschaetzung</span>
        <h2>{question.title}</h2>
        <p>{question.description}</p>
        <div className="scale-grid">
          {values.map((item) => (
            <button
              type="button"
              key={item}
              className={`scale-pill ${scaleValue === item ? "selected" : ""}`}
              onClick={() => onScale(item)}
            >
              {item}
            </button>
          ))}
        </div>
      </section>
    );
  }

  const isMulti = question.type === "MULTIPLE_CHOICE";

  const toggleValue = (nextValue: string) => {
    if (isMulti) {
      onSelect(
        value.includes(nextValue)
          ? value.filter((item) => item !== nextValue)
          : [...value, nextValue],
      );
      return;
    }

    onSelect([nextValue]);
  };

  return (
    <section className="card question-card">
      <span className="eyebrow">{isMulti ? "Mehrfachauswahl" : "Single Choice"}</span>
      <h2>{question.title}</h2>
      <p>{question.description}</p>
      <div className="option-grid">
        {question.options.map((option) => (
          <button
            type="button"
            key={option.id}
            className={`option-card ${value.includes(option.value) ? "selected" : ""}`}
            onClick={() => toggleValue(option.value)}
          >
            {option.label}
          </button>
        ))}
      </div>
    </section>
  );
}

import { useEffect, useMemo, useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import { api } from "../api/client";
import ProgressBar from "../components/ProgressBar";
import QuestionCard from "../components/QuestionCard";
import type { AnswerPayload, Question, Questionnaire } from "../types/api";

type AnswerState = Record<string, { selectedValues: string[]; scaleValue?: number }>;

export default function QuestionnairePage() {
  const { sessionId } = useParams();
  const navigate = useNavigate();
  const location = useLocation();
  const [questionnaire, setQuestionnaire] = useState<Questionnaire | null>(null);
  const [answers, setAnswers] = useState<AnswerState>({});
  const [step, setStep] = useState(0);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");

  const tokenFromState = location.state?.resultToken as string | undefined;

  useEffect(() => {
    api.getQuestionnaire()
      .then(setQuestionnaire)
      .catch((err) => setError(err instanceof Error ? err.message : "Fragebogen konnte nicht geladen werden."));
  }, []);

  const currentQuestion = questionnaire?.questions[step];

  const isAnswerValid = useMemo(() => {
    if (!currentQuestion) {
      return false;
    }
    const answer = answers[currentQuestion.key];
    if (currentQuestion.type === "SCALE") {
      return Boolean(answer?.scaleValue);
    }
    return (answer?.selectedValues?.length ?? 0) > 0;
  }, [answers, currentQuestion]);

  const updateAnswer = (question: Question, next: { selectedValues: string[]; scaleValue?: number }) => {
    setAnswers((previous) => ({
      ...previous,
      [question.key]: next,
    }));
  };

  const handleSubmit = async () => {
    if (!sessionId) {
      return;
    }
    setSubmitting(true);
    setError("");
    try {
      const payload: AnswerPayload[] = questionnaire?.questions.map((question) => ({
        questionKey: question.key,
        selectedValues: answers[question.key]?.selectedValues ?? [],
        scaleValue: answers[question.key]?.scaleValue,
      })) ?? [];

      const result = await api.submitAnswers(sessionId, payload);
      navigate(`/results/${result.token}`);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Ergebnisse konnten nicht berechnet werden.");
    } finally {
      setSubmitting(false);
    }
  };

  if (!questionnaire || !currentQuestion) {
    return (
      <main className="page-shell narrow-shell">
        <section className="card">
          <h1>Fragebogen wird geladen</h1>
          <p>{error || "Die Fragen werden vorbereitet."}</p>
        </section>
      </main>
    );
  }

  return (
    <main className="page-shell narrow-shell">
      <section className="wizard-head">
        <div>
          <span className="eyebrow">Questionnaire</span>
          <h1>{questionnaire.title}</h1>
          <p>{questionnaire.subtitle}</p>
          {tokenFromState ? <small className="muted">Ergebnis-Link wird nach dem Absenden unter deinem persoenlichen Token gespeichert.</small> : null}
        </div>
        <ProgressBar current={step + 1} total={questionnaire.questions.length} />
      </section>

      <QuestionCard
        question={currentQuestion}
        value={answers[currentQuestion.key]?.selectedValues ?? []}
        scaleValue={answers[currentQuestion.key]?.scaleValue}
        onSelect={(selectedValues) => updateAnswer(currentQuestion, { selectedValues })}
        onScale={(scaleValue) => updateAnswer(currentQuestion, {
          selectedValues: [String(scaleValue)],
          scaleValue,
        })}
      />

      <div className="wizard-actions">
        <button
          type="button"
          className="ghost-button"
          onClick={() => setStep((previous) => Math.max(0, previous - 1))}
          disabled={step === 0}
        >
          Zurueck
        </button>
        {step < questionnaire.questions.length - 1 ? (
          <button
            type="button"
            className="primary-button"
            onClick={() => setStep((previous) => previous + 1)}
            disabled={!isAnswerValid}
          >
            Weiter
          </button>
        ) : (
          <button
            type="button"
            className="primary-button"
            onClick={handleSubmit}
            disabled={!isAnswerValid || submitting}
          >
            {submitting ? "Matching laeuft..." : "Ergebnisse berechnen"}
          </button>
        )}
      </div>

      {error ? <p className="error-text">{error}</p> : null}
    </main>
  );
}

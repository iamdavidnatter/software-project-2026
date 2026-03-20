import type {
  AnswerPayload,
  Questionnaire,
  Result,
  Session,
  StudyProgram,
} from "../types/api";

const API_BASE = import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8080/api";

async function request<T>(path: string, init?: RequestInit): Promise<T> {
  const response = await fetch(`${API_BASE}${path}`, {
    headers: {
      "Content-Type": "application/json",
      ...(init?.headers ?? {}),
    },
    ...init,
  });

  if (!response.ok) {
    const error = await response.json().catch(() => null);
    const detail = error?.details?.join(", ") ?? "Unbekannter Fehler";
    throw new Error(detail);
  }

  const contentType = response.headers.get("content-type");
  if (contentType?.includes("application/pdf")) {
    return (await response.blob()) as T;
  }

  return response.json();
}

export const api = {
  getQuestionnaire: () => request<Questionnaire>("/questionnaire"),
  createSession: (nickname: string, availableHoursPerWeek: number) =>
    request<Session>("/sessions", {
      method: "POST",
      body: JSON.stringify({ nickname, availableHoursPerWeek }),
    }),
  submitAnswers: (sessionId: string, answers: AnswerPayload[]) =>
    request<Result>(`/sessions/${sessionId}/answers`, {
      method: "POST",
      body: JSON.stringify({ answers }),
    }),
  getResult: (token: string) => request<Result>(`/results/${token}`),
  updateFavorites: (token: string, programIds: number[]) =>
    request<{ favoriteProgramIds: number[] }>(`/results/${token}/favorites`, {
      method: "PUT",
      body: JSON.stringify({ programIds }),
    }),
  getPrograms: () => request<StudyProgram[]>("/study-programs"),
  comparePrograms: (programIds: number[]) =>
    request<StudyProgram[]>("/study-programs/compare", {
      method: "POST",
      body: JSON.stringify({ programIds }),
    }),
  downloadPdf: async (token: string) =>
    request<Blob>(`/results/${token}/pdf`, {
      headers: {},
    }),
};

import { Route, Routes } from "react-router-dom";
import LandingPage from "./pages/LandingPage";
import QuestionnairePage from "./pages/QuestionnairePage";
import ResultsPage from "./pages/ResultsPage";

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<LandingPage />} />
      <Route path="/questionnaire/:sessionId" element={<QuestionnairePage />} />
      <Route path="/results/:token" element={<ResultsPage />} />
    </Routes>
  );
}

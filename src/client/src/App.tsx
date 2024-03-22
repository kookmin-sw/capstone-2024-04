import { BrowserRouter, Route, Routes } from "react-router-dom";
import MainPage from "./pages/main/main";

function App() {
  return (
    <>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<MainPage />} />
        </Routes>
      </BrowserRouter>
    </>
  );
}

export default App;

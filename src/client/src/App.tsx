import { BrowserRouter, Route, Routes } from "react-router-dom";
import MainPage from "./pages/main/main";
import HomePage from "./pages/home/home";

function App() {
  return (
    <>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<MainPage />} />
          <Route path="/home" element={<HomePage />} />
        </Routes>
      </BrowserRouter>
    </>
  );
}

export default App;

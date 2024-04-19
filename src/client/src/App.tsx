import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import MainPage from "./pages/main/main";
import HomePage from "./pages/home/home";
import Cookies from "universal-cookie";

interface ProtectedRouteProps {
  isAuthenticated: boolean;
  children: React.ReactNode;
}

const isAuthenticated = () => {
  const cookies = new Cookies();

  return (
    cookies.get("accessToken") !== undefined &&
    cookies.get("refreshToken") !== undefined
  );
};

const ProtectedRoute = ({ isAuthenticated, children }: ProtectedRouteProps) => {
  return isAuthenticated ? <>{children}</> : <Navigate to="/" />;
};
function App() {
  return (
    <>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<MainPage />} />
          <Route
            path="/home"
            element={
              <ProtectedRoute isAuthenticated={isAuthenticated()}>
                <HomePage />
              </ProtectedRoute>
            }
          />
        </Routes>
      </BrowserRouter>
    </>
  );
}

export default App;

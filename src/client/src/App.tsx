import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import MainPage from "./pages/main/main";
import HomePage from "./pages/home/home";
import Cookies from "universal-cookie";
import AdminPage from "./pages/admin/admin";
import { QueryClient, QueryClientProvider } from "react-query";
import PlayListScreen from "./pages/playlist/playlist";

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

const isAdmin = () => {
  const cookies = new Cookies();

  const role = cookies.get("role") as string;

  if (!role || !role.includes("ADMIN")) return false;

  return (
    cookies.get("accessToken") !== undefined &&
    cookies.get("refreshToken") !== undefined
  );
};

const ProtectedRoute = ({ isAuthenticated, children }: ProtectedRouteProps) => {
  return isAuthenticated ? <>{children}</> : <Navigate to="/" />;
};

const queryClient = new QueryClient();

function App() {
  return (
    <QueryClientProvider client={queryClient} contextSharing={true}>
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
          <Route
            path="/admin"
            element={
              <ProtectedRoute isAuthenticated={isAdmin()}>
                <AdminPage />
              </ProtectedRoute>
            }
          />
          <Route path="/playlist/:locationId" element={<PlayListScreen />} />
          <Route path="*" element={<Navigate to="/" />} />
        </Routes>
      </BrowserRouter>
    </QueryClientProvider>
  );
}

export default App;

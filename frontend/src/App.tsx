import { Navigate, Route, Routes } from "react-router-dom";
import { useAuth } from "./auth/AuthContext";
import LoginPage from "./pages/LoginPage";
import ShellLayout from "./layouts/ShellLayout";
import DashboardPage from "./pages/DashboardPage";
import DataPage from "./pages/DataPage";
import UnauthorizedPage from "./pages/UnauthorizedPage";

export default function App() {
  const { principal } = useAuth();
  return (
    <Routes>
      <Route path="/login" element={principal ? <Navigate to="/" /> : <LoginPage />} />
      <Route path="/unauthorized" element={<UnauthorizedPage />} />
      {principal ? (
        <Route element={<ShellLayout />}>
          <Route index element={<DashboardPage />} />
          {principal.role !== "EMPLOYEE" && <Route path="companies" element={<DataPage kind="companies" />} />}
          <Route path="vehicles" element={<DataPage kind="vehicles" />} />
          <Route path="drivers" element={<DataPage kind="drivers" />} />
          <Route path="customers" element={<DataPage kind="customers" />} />
          <Route path="bookings" element={<DataPage kind="bookings" />} />
          <Route path="payments" element={<DataPage kind="payments" />} />
          <Route path="trips" element={<DataPage kind="trips" />} />
        </Route>
      ) : (
        <Route path="*" element={<Navigate to="/login" replace />} />
      )}
      <Route path="*" element={<div>Not found</div>} />
    </Routes>
  );
}

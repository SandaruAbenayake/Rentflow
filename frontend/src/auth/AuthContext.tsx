import { createContext, useContext, useMemo, useState, type ReactNode } from "react";

export type Role = "SUPER_ADMIN" | "COMPANY_ADMIN" | "EMPLOYEE";
export interface Principal { id: string; email: string; fullName: string; role: Role; companyId: string }
interface AuthState {
  principal: Principal | null;
  accessToken: string | null;
  refreshToken: string | null;
  login: (email: string, password: string) => Promise<void>;
  logout: () => Promise<void>;
}
const AuthContext = createContext<AuthState | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [principal, setPrincipal] = useState<Principal | null>(() => {
    const value = sessionStorage.getItem("rentflow-principal");
    return value ? JSON.parse(value) : null;
  });
  const [accessToken, setAccessToken] = useState<string | null>(() => sessionStorage.getItem("rentflow-access"));
  const [refreshToken, setRefreshToken] = useState<string | null>(() => sessionStorage.getItem("rentflow-refresh"));

  async function login(email: string, password: string) {
    const response = await fetch("/api/v1/auth/login", {
      method: "POST", headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password }),
    });
    if (!response.ok) throw new Error("Invalid email or password");
    const data = await response.json();
    setPrincipal(data.user); setAccessToken(data.accessToken); setRefreshToken(data.refreshToken);
    sessionStorage.setItem("rentflow-principal", JSON.stringify(data.user));
    sessionStorage.setItem("rentflow-access", data.accessToken);
    sessionStorage.setItem("rentflow-refresh", data.refreshToken);
  }
  async function logout() {
    if (refreshToken) await fetch("/api/v1/auth/logout", { method: "POST", headers: { "Content-Type": "application/json" }, body: JSON.stringify({ refreshToken }) }).catch(() => undefined);
    setPrincipal(null); setAccessToken(null); setRefreshToken(null);
    ["rentflow-principal","rentflow-access","rentflow-refresh"].forEach(key => sessionStorage.removeItem(key));
  }
  const value = useMemo(() => ({ principal, accessToken, refreshToken, login, logout }), [principal, accessToken, refreshToken]);
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
export function useAuth() {
  const value = useContext(AuthContext);
  if (!value) throw new Error("useAuth requires AuthProvider");
  return value;
}

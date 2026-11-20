import type { Role } from "./AuthContext";

export function canManageCompanies(role: Role): boolean {
  return role === "SUPER_ADMIN";
}

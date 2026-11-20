import { describe, expect, it } from "vitest";
import { canManageCompanies } from "./roles";

describe("role navigation", () => {
  it("only allows super admins to manage companies", () => {
    expect(canManageCompanies("SUPER_ADMIN")).toBe(true);
    expect(canManageCompanies("COMPANY_ADMIN")).toBe(false);
    expect(canManageCompanies("EMPLOYEE")).toBe(false);
  });
});

/** Matches backend AccountEntity.Role (no ROLE_ prefix in API body). */
export const ROLE_USER = "USER";
export const ROLE_ADMIN = "ADMIN";

const AUTH_KEY = "pilot.auth";

function emitAuthChanged() {
  window.dispatchEvent(new CustomEvent("pilot-auth-changed"));
}

/**
 * @returns {{ token: string, accountName: string, role: string } | null}
 */
export function getStoredAuth() {
  try {
    const raw = localStorage.getItem(AUTH_KEY);
    if (!raw) return null;
    const parsed = JSON.parse(raw);
    if (
      parsed &&
      typeof parsed.token === "string" &&
      typeof parsed.accountName === "string" &&
      typeof parsed.role === "string"
    ) {
      return parsed;
    }
    return null;
  } catch {
    return null;
  }
}

export function getToken() {
  return getStoredAuth()?.token ?? null;
}

/** @param {{ token: string, accountName: string, role: string }} auth */
export function saveAuth(auth) {
  localStorage.setItem(AUTH_KEY, JSON.stringify(auth));
  emitAuthChanged();
}

export function clearAuth() {
  localStorage.removeItem(AUTH_KEY);
  emitAuthChanged();
}

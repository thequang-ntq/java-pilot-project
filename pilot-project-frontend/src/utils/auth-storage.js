import { jwtDecode } from "jwt-decode";

// Manage access token and refresh token in local storage
// Save 2 tokens in local storage
// Decode access token to get username and role

// Declare
const ACCESS_TOKEN_KEY = "access_token";
const REFRESH_TOKEN_KEY = "refresh_token";

// Get
export const getToken = () => localStorage.getItem(ACCESS_TOKEN_KEY);
export const getRefreshToken = () => localStorage.getItem(REFRESH_TOKEN_KEY);

export const getUsername = () => {
  const token = getToken();
  if (!token) return null;
  try {
    const decoded = jwtDecode(token);
    return decoded.sub; // subject
  } catch {
    return null;
  }
};

export const getRole = () => {
  const token = getToken();
  if (!token) return null;
  try {
    const decoded = jwtDecode(token);
    return decoded.role; // role claim
  } catch {
    return null;
  }
};

// Set
export const setAuth = (accessToken, refreshToken) => {
  localStorage.setItem(ACCESS_TOKEN_KEY, accessToken);
  localStorage.setItem(REFRESH_TOKEN_KEY, refreshToken);
};

// Remove
export const clearAuth = () => {
  localStorage.removeItem(ACCESS_TOKEN_KEY);
  localStorage.removeItem(REFRESH_TOKEN_KEY);
};

// Check if login -> Has token and role not empty
export const isAuthenticated = () => {
  return !!getToken() && !!getRole();
};

// Check role
export const isAdmin = () => {
  return getRole() === "ADMIN";
};

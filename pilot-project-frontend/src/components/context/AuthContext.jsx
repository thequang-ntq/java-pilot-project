import { useState } from "react";
import { AuthContext } from "./auth-context.js";
import {
  getUsername,
  getRole,
  isAuthenticated,
  setAuth,
  clearAuth,
} from "../../services/auth-storage.js";

// Component fully wrapped app to provide authentication (login), use in App.jsx
export default function AuthProvider({ children }) {
  // State to save current user, auth: username and role
  const [auth, setAuthState] = useState(() => {
    if (isAuthenticated()) {
      return {
        username: getUsername(),
        role: getRole(),
      };
    }
    return null;
  });

  // Login function, turn data into JSON, save into localStorage be key: access_token and refresh_token
  const loginContext = (accessToken, refreshToken) => {
    setAuth(accessToken, refreshToken);
    setAuthState({
      username: getUsername(),
      role: getRole(),
    });
  };

  // Logout, remove from localStorage, set state
  const logoutContext = () => {
    clearAuth();
    setAuthState(null);
  };

  // Give provider for all components to have: useContext(AuthContext)
  // auth = JSON object of current user: username, role. Login, logout = functions
  return (
    <AuthContext.Provider value={{ auth, loginContext, logoutContext }}>
      {children}
    </AuthContext.Provider>
  );
}

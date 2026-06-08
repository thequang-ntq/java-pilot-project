import { createContext } from "react";

// Shape: { username: string, role: "ADMIN"|"USER" } | null
// Context of current user that every component can use (because use in App.jsx)
export const AuthContext = createContext(null);

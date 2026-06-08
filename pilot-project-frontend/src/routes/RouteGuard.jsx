import { Navigate } from "react-router-dom";
import useAuth from "../context/use-auth";

// Define which role and which type of user (Guest, Admin) can access which routes (IMPORTANT)
// Children = route to go, requiredRole = role need to go to children role
// Block all not valid authenticate/authorize before going to the route
// What route be limited? -> Login (Guest), Admin pages (Admin), default
// page (Dashboard), Logout modal (Admin)

// Default route for Guest/Admin
export function RootRedirect() {
  return <Navigate to="/dashboard" replace />;
}

// Only guest can access, also define other authenticate/authorize must go to which route
// For login, signup
export function GuestRoute({ children }) {
  const { auth } = useAuth();

  // Authenticated (Admin, ...) -> DashboardPage
  if (auth) return <Navigate to="/dashboard" replace />;

  // Guest -> Login
  return children;
}

// Only admin can access, also define other
// For admin pages
export function AdminRoute({ children }) {
  const { auth } = useAuth();

  // Guest -> Login
  if (!auth) return <Navigate to="/login" replace />;

  // Not admin -> Dashboard
  if (auth && auth?.role !== "ADMIN")
    return <Navigate to="/dashboard" replace />;

  return children;
}

// The general route guard
export function ProtectedRoute({ children, allowedRoles }) {
  const { auth } = useAuth();

  // Guest
  if (!auth) return <Navigate to="/login" replace />;

  // Admin -> have role, check if allowedRoles has elements and allowedRoles contains Admin role
  // if no elements in allowedRoles -> all can access, if has -> check if contains Admin role
  const permitted = !allowedRoles?.length || allowedRoles?.includes(auth?.role);
  if (!permitted) {
    // Authenticated (Admin, ...)
    return <Navigate to="/dashboard" replace />;
  }

  return children;
}

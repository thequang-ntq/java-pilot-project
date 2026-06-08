import "./App.css";
import {
  BrowserRouter,
  Routes,
  Route,
  useLocation,
  useNavigate,
} from "react-router-dom";
import { RootRedirect, GuestRoute, AdminRoute } from "./routes/RouteGuard";
import { useEffect } from "react";
import AuthProvider from "./context/AuthContext";
import BrandsPage from "./pages/brands/BrandsPage";
import ProductsPage from "./pages/products/ProductsPage";
import LoginPage from "./pages/login/LoginPage";
import DashboardPage from "./pages/dashboard/DashboardPage";
import BrandFormPage from "./pages/brands/BrandFormPage";
import ProductFormPage from "./pages/products/ProductFormPage";

function ScrollToTop() {
  const navigate = useNavigate();
  const { pathname } = useLocation();

  // Change route -> Scroll to top
  useEffect(() => {
    window.scrollTo({ top: 0, left: 0, behavior: "smooth" });
  }, [pathname]);

  // Unauthorized (HTTP Status 401) -> navigate /login
  useEffect(() => {
    const handleUnauthorized = () => {
      navigate("/login", { replace: true });
    };

    window.addEventListener("unauthorized", handleUnauthorized);

    return () => {
      window.removeEventListener("unauthorized", handleUnauthorized);
    };
  }, [navigate]);

  return null;
}

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <ScrollToTop />
        <Routes>
          <Route path="/" element={<RootRedirect />}></Route>
          {/* Guest Routes, dashboard, brands and products for both Guest and Admin*/}
          <Route path="/dashboard" element={<DashboardPage />}></Route>
          <Route path="/brands" element={<BrandsPage />}></Route>
          <Route path="/products" element={<ProductsPage />}></Route>
          <Route
            path="/login"
            element={
              <GuestRoute>
                <LoginPage />
              </GuestRoute>
            }
          ></Route>

          {/* Admin Routes */}
          <Route
            path="/brands/add"
            element={
              <AdminRoute>
                <BrandFormPage />
              </AdminRoute>
            }
          ></Route>
          <Route
            path="/brands/:id/edit"
            element={
              <AdminRoute>
                <BrandFormPage />
              </AdminRoute>
            }
          ></Route>
          <Route
            path="/products/add"
            element={
              <AdminRoute>
                <ProductFormPage />
              </AdminRoute>
            }
          ></Route>
          <Route
            path="/products/:id/edit"
            element={
              <AdminRoute>
                <ProductFormPage />
              </AdminRoute>
            }
          ></Route>

          {/* Not found routes -> Go to Dashboard */}
          <Route path="/*" element={<RootRedirect />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

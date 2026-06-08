import "./App.css";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import HomePage from "./pages/home/HomePage";
import ProductsPage from "./pages/products/ProductsPage";
import BrandsPage from "./pages/brands/BrandsPage";
import LoginPage from "./pages/login/LoginPage";
import RegisterPage from "./pages/register/RegisterPage";
import CartPage from "./pages/cart/CartPage";
import OrderHistoryPage from "./pages/history/HistoryPage";
import OrdersPage from "./pages/orders/OrdersPage";
import NotFoundPage from "./pages/not-found/NotFoundPage";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/home" />}></Route>
        {/* Guest Routes*/}
        <Route path="/home" element={<HomePage />}></Route>
        <Route path="/products" element={<ProductsPage />}></Route>
        <Route path="/brands" element={<BrandsPage />}></Route>
        <Route path="/login" element={<LoginPage />}></Route>
        <Route path="/register" element={<RegisterPage />}></Route>
        {/* User Routes */}
        <Route path="/cart" element={<CartPage />}></Route>
        <Route path="/history" element={<OrderHistoryPage />}></Route>
        {/* Admin Routes */}
        <Route path="/orders" element={<OrdersPage />}></Route>
        {/* Other Routes */}
        <Route path="/*" element={<NotFoundPage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;

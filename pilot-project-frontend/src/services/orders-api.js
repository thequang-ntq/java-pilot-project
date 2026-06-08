import axiosInstance from "./axios-instance";

// --- USER (role USER) --------------------------------------------------------
export const getCart = (page = 1) =>
  axiosInstance.get("/cart", { params: { page } });

export const addToCart = (productId) =>
  axiosInstance.post(`/cart/products/${productId}`);

export const updateCartItem = (productId, quantity) =>
  axiosInstance.put(`/cart/products/${productId}`, null, {
    params: { quantity },
  });

export const removeCartItem = (productId) =>
  axiosInstance.delete(`/cart/products/${productId}`);

export const clearCart = () => axiosInstance.delete("/cart");

export const confirmOrder = () => axiosInstance.post("/cart/confirm");

export const getOrderHistory = (params) =>
  axiosInstance.get("/orders", { params });

export const getOrderDetail = (orderId, page = 1) =>
  axiosInstance.get(`/orders/${orderId}`, { params: { page } });

// --- ADMIN (role ADMIN) --------------------------------------------------------

export const getAdminOrders = (params) =>
  axiosInstance.get("/admin/orders", { params });

export const getAdminOrderDetail = (orderId, page = 1) =>
  axiosInstance.get(`/admin/orders/${orderId}`, { params: { page } });

export const updateOrderStatus = (orderId, status) =>
  axiosInstance.put(`/admin/orders/${orderId}/status`, null, {
    params: { status },
  });

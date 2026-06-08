import axiosInstance from "./axios-instance";

export const getProducts = (params) =>
  axiosInstance.get("/products", { params });

export const getProductById = (id) => axiosInstance.get(`/products/${id}`);

/** Admin — send FormData (do not set Content-Type; browser sets multipart boundary). */
export const createProduct = (formData) =>
  axiosInstance.post("/admin/products", formData);

export const updateProduct = (formData) =>
  axiosInstance.put("/admin/products", formData);

export const deleteProduct = (id) =>
  axiosInstance.delete(`/admin/products/${id}`);

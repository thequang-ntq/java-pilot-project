import axiosInstance from "./axios-instance";

export const getProducts = (params) =>
  axiosInstance.get("/products", { params });

export const getProductById = (id) => axiosInstance.get(`/products/${id}`);

export const createProduct = (formData) =>
  axiosInstance.post("/products", formData);

export const updateProduct = (id, formData) =>
  axiosInstance.put(`/products/${id}`, formData);

export const deleteProduct = (id) => axiosInstance.delete(`/products/${id}`);

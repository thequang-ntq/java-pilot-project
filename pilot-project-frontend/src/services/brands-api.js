import axiosInstance from "./axios-instance";

export const getBrands = (params) => axiosInstance.get("/brands", { params });

export const getBrandById = (id) => axiosInstance.get(`/brands/${id}`);

export const createBrand = (formData) =>
  axiosInstance.post("/brands", formData);

export const updateBrand = (id, formData) =>
  axiosInstance.put(`/brands/${id}`, formData);

export const deleteBrand = (id) => axiosInstance.delete(`/brands/${id}`);

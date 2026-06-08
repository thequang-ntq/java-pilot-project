import axiosInstance from "./axios-instance";

export const getBrands = (params) => axiosInstance.get("/brands", { params });

export const getBrandById = (id) => axiosInstance.get(`/brands/${id}`);

export const createBrand = (formData) =>
  axiosInstance.post("/admin/brands", formData);

export const updateBrand = (formData) =>
  axiosInstance.put("/admin/brands", formData);

export const deleteBrand = (id) =>
  axiosInstance.delete(`/admin/brands/${id}`);

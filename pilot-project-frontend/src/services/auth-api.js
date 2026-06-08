import axiosInstance from "./axios-instance";

export const login = (body) => axiosInstance.post("/auth/login", body);

export const refresh = (body) => axiosInstance.post("/auth/refresh", body);

export const logout = () => axiosInstance.post("/auth/logout");

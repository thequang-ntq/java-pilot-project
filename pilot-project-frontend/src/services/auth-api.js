import axiosInstance from "./axios-instance";

export const login = (body) => axiosInstance.post("/auth/login", body);

export const register = (body) => axiosInstance.post("/auth/register", body);

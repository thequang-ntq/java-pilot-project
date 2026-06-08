import axios from "axios";
import { clearAuth, getToken } from "./auth-storage";

const axiosInstance = axios.create({
  baseURL: "http://localhost:8080/api",
  timeout: 10000,
  headers: { "Content-Type": "application/json" },
});

axiosInstance.interceptors.request.use((config) => {
  const token = getToken();
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  if (config.data instanceof FormData) {
    delete config.headers["Content-Type"];
  }
  return config;
});

axiosInstance.interceptors.response.use(
  (res) => res,
  (err) => {
    const status = err.response?.status;
    if (status === 401) {
      clearAuth();
    }
    if (status === 400) {
      err.userMessage = "Data / id is not valid";
    } else if (status === 403) {
      err.userMessage = "You do not have permission";
    } else if (status === 404) {
      err.userMessage = "Not found";
    } else if (status >= 500) {
      err.userMessage = "Server error";
    } else if (!err.response) {
      err.userMessage = "Network error";
    } else {
      err.userMessage = "Request failed";
    }
    return Promise.reject(err);
  },
);

export default axiosInstance;

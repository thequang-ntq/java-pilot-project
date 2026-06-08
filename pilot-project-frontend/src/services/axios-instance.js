import axios from "axios";
import { clearAuth, getToken, getRefreshToken, setAuth } from "./auth-storage";
import { BASE_URL } from "../utils/constants";

// Axios instance for API call, base url is prefix, timeout -> request after 10s is fail, default JSON type for request
const axiosInstance = axios.create({
  baseURL: `${BASE_URL}/api`,
  timeout: 10000,
  headers: { "Content-Type": "application/json" },
});

let isRefreshing = false; // Check if there is a refresh request running
let failedQueue = []; // List of requests that is waiting for new access token

// Run old requests with new access token (refresh success), otherwise reject all
// For Add/Update/Delete requests (protected) that required valid access token, but expired -> call refresh request and
// push Add/Update/Delete request in queue.
const processQueue = (error, token = null) => {
  failedQueue.forEach((prom) => {
    if (error) {
      prom.reject(error);
    } else {
      prom.resolve(token);
    }
  });
  failedQueue = [];
};

// Interceptors run before all requests.
axiosInstance.interceptors.request.use((config) => {
  const token = getToken(); // Access token (Jwt Token)
  if (token) {
    config.headers.Authorization = `Bearer ${token}`; // Set header Authorization
  }

  // If request is FormData (upload file for add/edit in Brand, Product) -> delete Content-Type, no JSON
  if (config.data instanceof FormData) {
    delete config.headers["Content-Type"];
  }
  return config;
});

// Interceptors run before all responses.
axiosInstance.interceptors.response.use(
  // Success
  (res) => {
    // If not 200 -> Business error
    const { responseCode, responseMsg } = res.data;

    // Business error
    if (responseCode !== 200) {
      const err = new Error(responseMsg);

      err.response = res;
      err.responseCode = responseCode;
      err.userMessage = responseMsg;

      // Check if business error or HTTP status
      err.isBusinessError = true;

      return Promise.reject(err);
    }

    // Real success
    return res;
  },

  // Error: 401, 403
  async (err) => {
    // Status code
    const status = err.response?.status;

    // HTTP status 401
    if (status === 401 && !err.isBusinessError) {
      // access token
      const token = getToken();
      // If not login
      if (!token) {
        err.userMessage = "You do not login yet";
        // RouteGuard auto navigate /login...
      } else {
        // Get present request to retry after refresh
        const originalRequest = err.config;

        // Check if this request has been retried after refresh yet?
        if (!originalRequest._retry) {
          // Check if any refresh request is running
          if (isRefreshing) {
            // Add request to queue
            return (
              new Promise((resolve, reject) => {
                failedQueue.push({ resolve, reject });
              })
                // Done refresh
                .then((token) => {
                  originalRequest.headers.Authorization = `Bearer ${token}`;
                  // Retry request
                  return axiosInstance(originalRequest);
                })
                .catch((err) => {
                  return Promise.reject(err);
                })
            );
          }

          // Set: request has been retried, refresh request is running
          originalRequest._retry = true;
          isRefreshing = true;

          // Get refresh token, if not have then logout
          const refreshToken = getRefreshToken();
          if (!refreshToken) {
            clearAuth();
            window.dispatchEvent(new Event("unauthorized"));
            return Promise.reject(err);
          }

          try {
            // Call refresh API, if using axios instance -> loop
            const response = await axios.post(`${BASE_URL}/api/auth/refresh`, {
              refreshToken,
            });

            if (response.data.responseCode === 200) {
              // Save new tokens and set local storage
              const { accessToken, refreshToken } = response.data.data;
              setAuth(accessToken, refreshToken);

              originalRequest.headers.Authorization = `Bearer ${accessToken}`;

              // Run requests in queue and present request
              processQueue(null, accessToken);
              return axiosInstance(originalRequest);
            } else {
              clearAuth();
              window.dispatchEvent(new Event("unauthorized"));
              return Promise.reject(err);
            }
          } catch (refreshError) {
            processQueue(refreshError, null);
            clearAuth();
            window.dispatchEvent(new Event("unauthorized"));
            return Promise.reject(refreshError);
          } finally {
            isRefreshing = false;
          }
        }
      }
    } else if (status === 403) {
      err.userMessage = "You do not have permission";
    } else {
      // Business error / general error
      err.userMessage =
        err.userMessage || err.response?.data?.responseMsg || "Request failed";
    }
    return Promise.reject(err);
  },
);

export default axiosInstance;

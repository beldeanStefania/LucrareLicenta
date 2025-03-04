import axios from "axios";
import { jwtDecode } from "jwt-decode";

export const getAuthToken = () => {
  return window.localStorage.getItem("auth_token");
};

export const decodeToken = (token) => {
  if (!token) return null;
  try {
    return jwtDecode(token); // Decodes the JWT and returns the payload
  } catch (e) {
    console.error("Failed to decode token", e);
    return null;
  }
};

export const setAuthHeader = (token) => {
  if (token) {
    window.localStorage.setItem("auth_token", token);
    axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
  } else {
    delete axios.defaults.headers.common["Authorization"];
    window.localStorage.removeItem("auth_token");

  }
};


axios.defaults.baseURL = process.env.REACT_APP_API_BASE_URL || "http://176.34.129.151:8080";
axios.defaults.headers.post["Content-Type"] = "application/json";

export const request = (method, url, data) => {
  const token = getAuthToken();
  const headers = token ? { Authorization: `Bearer ${token}` } : {};
  return axios({
    method,
    url,
    headers,
    data,
  });
};


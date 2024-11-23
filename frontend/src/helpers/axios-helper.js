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
  if (token !== null) {
    window.localStorage.setItem("auth_token", token);
  } else {
    window.localStorage.removeItem("auth_token");
  }
};

axios.defaults.baseURL = "http://localhost:8080";
axios.defaults.headers.post["Content-Type"] = "application/json";

export const request = (method, url, data) => {
  let headers = {};
  if (getAuthToken() !== null && getAuthToken() !== "null") {
    headers = { Authorization: `Bearer ${getAuthToken()}` };
  }

  return axios({
    method: method,
    url: url,
    headers: headers,
    data: data,
  });
};

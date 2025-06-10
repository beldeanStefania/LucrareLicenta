import axios from "axios";
import jwt_decode from "jwt-decode";

export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://54.155.59.152:30081";
axios.defaults.baseURL = API_BASE_URL;

// Obține tokenul din localStorage
export const getAuthToken = () => {
  const token = window.localStorage.getItem("auth_token");
  if (token && token.trim() === "") {
    window.localStorage.removeItem("auth_token");
    return null;
  }
  return token;
};

// Decode JWT
export const decodeToken = (token) => {
  if (!token) return null;

  // Check if token has a valid format (at least has two dots for three parts)
  if (!token.includes('.') || token.split('.').length !== 3) {
    console.error("Invalid token format - must have header, payload, and signature parts");
    return null;
  }
  try {
    console.log("Token to decode:", token);
    const decoded = jwt_decode(token);
    console.log("Decoded token:", decoded);
    return decoded;
  } catch (e) {
    console.error("Failed to decode token", e);
    console.error("Token that failed to decode:", token);
    return null;
  }
};

// Setează header-ul de autorizare pentru toate cererile
export const setAuthHeader = (token) => {
  if (token) {
    window.localStorage.setItem("auth_token", token);
    axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
  } else {
    delete axios.defaults.headers.common["Authorization"];
    window.localStorage.removeItem("auth_token");
  }
};

export function request(method, url, data = null) {
  const token = window.localStorage.getItem("auth_token");
  const headers = token ? { Authorization: `Bearer ${token}` } : {};

  if (data instanceof FormData) {
  } else if (data != null) {
    headers["Content-Type"] = "application/json";
  }

  return axios({
    method,
    url,                     
    data,
    headers,
    withCredentials: true    
  });
}


export function requestBlob(method, url, data = null) {
  const token = window.localStorage.getItem("auth_token");
  const headers = {
    ...(token ? { Authorization: `Bearer ${token}` } : {})
  };

  return axios({
    method,
    url,
    data,
    headers,
    responseType: "blob"
  });
}

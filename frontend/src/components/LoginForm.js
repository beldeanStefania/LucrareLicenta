import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { request, decodeToken, setAuthHeader } from "../helpers/axios-helper";
import "./LoginForm.css";

export default function LoginForm({ onLogin }) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleLogin = (e) => {
    e.preventDefault();
  
    request("POST", "/api/auth/login", { username, password })
      .then((response) => {
        const token = response.data.token;
        setAuthHeader(token);
        localStorage.setItem("auth_token", token);
  
        const decodedToken = decodeToken(token);
        const role = decodedToken?.role;
  
        if (role === "ROLE_ADMIN") {
          navigate("/admin");
        } else if (role === "ROLE_STUDENT") {
          navigate("/student");
        } else if (role === "ROLE_PROFESOR") {
          navigate("/profesor"); // Redirecționăm către pagina profesorului
        } else {
          alert("Unknown role. Please contact the administrator.");
        }
  
        onLogin();
      })
      .catch((error) => {
        console.error("Failed to login:", error);
        alert("Invalid credentials. Please try again.");
      });
  };
  

  return (
    <div className="login-container">
      <h1>Login</h1>
      <form onSubmit={handleLogin} className="login-form">
        <div className="form-group">
          <label>Username:</label>
          <input
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
        </div>
        <div className="form-group">
          <label>Password:</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        <button type="submit" className="btn btn-primary">Login</button>
      </form>
    </div>
  );
}

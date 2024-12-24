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

    // Autentificare utilizator
    request("POST", "/api/auth/login", { username, password })
      .then((response) => {
        const token = response.data.token; // Primim token-ul JWT
        setAuthHeader(token); // Salvăm token-ul în antet
        localStorage.setItem("auth_token", token); // Salvăm token-ul în localStorage

        const decodedToken = decodeToken(token); // Decodificăm token-ul pentru a obține rolul
        const role = decodedToken?.role;

        if (role === "ROLE_ADMIN") {
          navigate("/admin"); // Redirecționăm către AdminPage
        } else if (role === "ROLE_STUDENT") {
          navigate("/student"); // Redirecționăm către StudentPage
        } else {
          alert("Unknown role. Please contact the administrator.");
        }

        onLogin(); // Actualizăm starea de autentificare
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

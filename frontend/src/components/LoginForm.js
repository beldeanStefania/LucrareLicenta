import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { request, setAuthHeader } from "../helpers/axios-helper";
import "./LoginForm.css";

export default function LoginForm({ onLogin }) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleLogin = (e) => {
    e.preventDefault();

    // Trimite cererea de login către backend
    request("POST", "/api/auth/login", { username, password })
      .then((response) => {
        const token = response.data.token; // Preluăm token-ul din răspuns
        setAuthHeader(token); // Setăm token-ul în antet pentru cereri viitoare
        localStorage.setItem("auth_token", token); // Salvăm token-ul în localStorage
        //alert("Login successful!");
        onLogin(); // Actualizăm starea globală de autentificare
        navigate("/admin"); // Navigăm către dashboard-ul Admin
      })
      .catch((error) => {
        console.error("Login failed:", error);
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

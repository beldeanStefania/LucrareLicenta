import React, { useState, useEffect } from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import AdminPage from "./AdminPage";
import StudentPage from "./StudentPage";
import LoginForm from "./LoginForm";
import WelcomePage from "./WelcomePage.js";
import { getAuthToken, setAuthHeader } from "../helpers/axios-helper";
import "./App.css";

export default function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  useEffect(() => {
    // Verificăm dacă există un token de autentificare
    const token = getAuthToken();
    setIsLoggedIn(!!token); // Setăm autentificarea în funcție de token
    if (token) {
      setAuthHeader(token); // Setăm antetul pentru cereri
    }
  }, []);

  const handleLogin = () => {
    setIsLoggedIn(true);
  };

  const handleLogout = () => {
    localStorage.removeItem("auth_token");
    setAuthHeader(null);
    setIsLoggedIn(false);
  };

  return (
    <Router>
      <Routes>
        <Route path="/" element={<WelcomePage />} />
        <Route path="/login" element={<LoginForm onLogin={handleLogin} />} />
        <Route
          path="/admin"
          element={
            isLoggedIn ? (
              <AdminPage onLogout={handleLogout} />
            ) : (
              <Navigate to="/login" />
            )
          }
        />
        <Route
          path="/student"
          element={
            isLoggedIn ? (
              <StudentPage onLogout={handleLogout} />
            ) : (
              <Navigate to="/login" />
            )
          }
        />
      </Routes>
    </Router>
  );
}

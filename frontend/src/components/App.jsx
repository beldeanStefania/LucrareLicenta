import React, { useState, useEffect } from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import AdminPage from "./AdminPage.jsx";
import StudentPage from "./StudentPage.jsx";
import ProfessorPage from "./ProfessorPage.jsx"; // Importăm pagina pentru profesori
import LoginForm from "./LoginForm.jsx";
import WelcomePage from "./WelcomePage.jsx";
import { getAuthToken, setAuthHeader, decodeToken } from "../helpers/axios-helper.jsx";
import "./App.css";

export default function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [userRole, setUserRole] = useState(null);
  const [authChecked, setAuthChecked] = useState(false);

  useEffect(() => {
    const token = getAuthToken();
    if (token) {
      setAuthHeader(token);
      const decodedToken = decodeToken(token);
      console.log("Decoded token:", decodedToken);
      setUserRole(decodedToken?.role || null);
      setIsLoggedIn(true);
    }
    setAuthChecked(true);
  }, []);
  
  if (!authChecked) {
    return <div>Loading...</div>;
  }

  const handleLogin = () => {
    const token = getAuthToken();
    if (token) {
      setIsLoggedIn(true);
      setAuthHeader(token);

      const decodedToken = decodeToken(token);
      setUserRole(decodedToken?.role || null);
    }
  };

  const handleLogout = () => {
    localStorage.removeItem("auth_token");
    setAuthHeader(null);
    setIsLoggedIn(false);
    setUserRole(null);
  };

  return (
    <Router>
      <Routes>
        <Route path="/" element={<WelcomePage />} />
        <Route path="/login" element={<LoginForm onLogin={handleLogin} />} />
        <Route
          path="/admin"
          element={
            isLoggedIn && userRole === "ROLE_ADMIN" ? (
              <AdminPage onLogout={handleLogout} />
            ) : (
              <Navigate to="/login" />
            )
          }
        />
        <Route
          path="/student"
          element={
            isLoggedIn && userRole === "ROLE_STUDENT" ? (
              <StudentPage onLogout={handleLogout} />
            ) : (
              <Navigate to="/login" />
            )
          }
        />
        <Route
          path="/profesor"
          element={
            isLoggedIn && userRole === "ROLE_PROFESOR" ? (
              <ProfessorPage onLogout={handleLogout} />
            ) : (
              <Navigate to="/login" />
            )
          }
        />
      </Routes>
    </Router>
  );
}

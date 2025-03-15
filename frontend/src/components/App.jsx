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
    return <div className="loading-container">
      <div className="loading-spinner"></div>
      <div className="loading-text">Loading...</div>
    </div>;
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

  // Function to redirect user based on role after login
  const redirectBasedOnRole = () => {
    if (!isLoggedIn) return <Navigate to="/login" />;
    
    switch(userRole) {
      case "ROLE_ADMIN":
        return <Navigate to="/admin" />;
      case "ROLE_STUDENT":
        return <Navigate to="/student" />;
      case "ROLE_PROFESOR":
        return <Navigate to="/profesor" />;
      default:
        return <Navigate to="/login" />;
    }
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
        
        {/* Dashboard route for redirecting after login */}
        <Route path="/dashboard" element={redirectBasedOnRole()} />
        
        {/* Catch-all route to handle 404s - redirect to welcome page */}
        <Route path="*" element={<Navigate to="/" />} />
      </Routes>
    </Router>
  );
}

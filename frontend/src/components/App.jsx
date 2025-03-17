import React, { useState, useEffect } from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import AdminPage from "./AdminPage.jsx";
import StudentPage from "./StudentPage.jsx";
import ProfessorPage from "./ProfessorPage.jsx";
import LoginForm from "./LoginForm.jsx";
import WelcomePage from "./WelcomePage.jsx";
import { getAuthToken, setAuthHeader, decodeToken } from "../helpers/axios-helper.jsx";
import "./App.css";

export default function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [userRole, setUserRole] = useState(null);
  const [authChecked, setAuthChecked] = useState(false);

  useEffect(() => {
    const checkAuthStatus = () => {
      const token = getAuthToken();
      if (token) {
        setAuthHeader(token);
        try {
          const decodedToken = decodeToken(token);
          console.log("Decoded token:", decodedToken);
          setUserRole(decodedToken?.role || null);
          setIsLoggedIn(true);
        } catch (error) {
          console.error("Invalid token:", error);
          localStorage.removeItem("auth_token");
          setAuthHeader(null);
          setIsLoggedIn(false);
          setUserRole(null);
        }
      } else {
        setIsLoggedIn(false);
        setUserRole(null);
      }
      setAuthChecked(true);
    };

    checkAuthStatus();

    const handleStorageChange = (e) => {
      if (e.key === "auth_token") {
        checkAuthStatus();
      }
    };

    window.addEventListener("storage", handleStorageChange);
    return () => {
      window.removeEventListener("storage", handleStorageChange);
    };
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

  const redirectBasedOnRole = () => {
    if (!isLoggedIn) return <Navigate to="/login" />;
    
    switch(userRole) {
      case "ROLE_ADMIN":
        return <Navigate to="/admin" replace />;
      case "ROLE_STUDENT":
        return <Navigate to="/student" replace />;
      case "ROLE_PROFESOR":
        return <Navigate to="/profesor" replace />;
      default:
        return <Navigate to="/login" replace />;
    }
  };

  const redirectToLoginOrDashboard = () => {
    if (isLoggedIn) {
      return redirectBasedOnRole();
    } else {
      return <LoginForm onLogin={handleLogin} />;
    }
  };

  return (
    <Router>
      <Routes>
        <Route path="/" element={<WelcomePage />} />
        <Route path="/login" element={redirectToLoginOrDashboard()} />
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
        
        {}
        <Route path="/dashboard" element={redirectBasedOnRole()} />
        
        {}
        <Route path="*" element={isLoggedIn ? <Navigate to="/dashboard" /> : <Navigate to="/" />} />
      </Routes>
    </Router>
  );
}

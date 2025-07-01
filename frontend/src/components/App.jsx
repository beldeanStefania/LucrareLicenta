import React, { useState, useEffect } from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import AdminPage from "./AdminPage.jsx";
import StudentPage from "./StudentPage.jsx";
import ProfessorPage from "./ProfessorPage.jsx";
import LoginForm from "./LoginForm.jsx";
import LoginFallback from "./LoginFallback.jsx";
import DirectLogin from "./DirectLogin.jsx";
import WelcomePage from "./WelcomePage.jsx";
import ErrorBoundary from "./ErrorBoundary.jsx";
import ContractSelectionPage from "./ContractPage.jsx";

import { getAuthToken, setAuthHeader, decodeToken } from "../helpers/axios-helper.jsx";
import "./App.css";

export default function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [userRole, setUserRole] = useState(null);
  const [username, setUsername] = useState(null);
  const [authChecked, setAuthChecked] = useState(false);

  useEffect(() => {
    const checkAuthStatus = () => {
      const token = getAuthToken();
      if (token) {
        try {
          const decoded = decodeToken(token);
          setUserRole(decoded.role || null);
          setUsername(decoded.sub || null);  
          setIsLoggedIn(true);
        } catch (err) {
          localStorage.removeItem("auth_token");
          setIsLoggedIn(false);
          setUserRole(null);
          setUsername(null);
        }
      }
      setAuthChecked(true);
    };

    checkAuthStatus();
    window.addEventListener("storage", () => checkAuthStatus());
    return () => window.removeEventListener("storage", () => checkAuthStatus());
  }, []);
  // …

  
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
      return (
        <ErrorBoundary fallback={<LoginFallback onLogin={handleLogin} />}>
          {redirectBasedOnRole()}
        </ErrorBoundary>
      );
    } else {
      return (
        <ErrorBoundary fallback={<LoginFallback onLogin={handleLogin} />}>
          <LoginForm onLogin={handleLogin} />
        </ErrorBoundary>
      );
    }
  };

  return (
    <ErrorBoundary>
      <Router>
        <Routes>
          <Route path="/" element={<WelcomePage />} />
          {}
          <Route path="/login" element={<DirectLogin />} />
          <Route
            path="/admin"
            element={
              <ErrorBoundary>
                {isLoggedIn && userRole === "ROLE_ADMIN" ? (
                  <AdminPage onLogout={handleLogout}
                  currentUsername={username} />
                ) : (
                  <Navigate to="/login" />
                )}
              </ErrorBoundary>
            }
          />
          <Route
            path="/student"
            element={
              <ErrorBoundary>
                {isLoggedIn && userRole === "ROLE_STUDENT" ? (
                  <StudentPage onLogout={handleLogout} />
                ) : (
                  <Navigate to="/login" />
                )}
              </ErrorBoundary>
            }
          />
          <Route
            path="/profesor"
            element={
              <ErrorBoundary>
                {isLoggedIn && userRole === "ROLE_PROFESOR" ? (
                  <ProfessorPage onLogout={handleLogout} />
                ) : (
                  <Navigate to="/login" />
                )}
              </ErrorBoundary>
            }
          />
          
          <Route 
            path="/dashboard" 
            element={
              <ErrorBoundary fallback={<LoginFallback onLogin={handleLogin} />}>
                {redirectBasedOnRole()}
              </ErrorBoundary>
            } 
          />

<Route
  path="/contract/select"
  element={
    <ErrorBoundary>
      {isLoggedIn && userRole === "ROLE_STUDENT" ? (
        <ContractSelectionPage onLogout={handleLogout}/>
      ) : (
        <Navigate to="/login" />
      )}
    </ErrorBoundary>
  }
/>

          
          <Route path="*" element={isLoggedIn ? <Navigate to="/dashboard" /> : <Navigate to="/" />} />
        </Routes>
      </Router>
    </ErrorBoundary>
  );
}

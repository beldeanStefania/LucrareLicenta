import React, { useState } from "react";
import { Routes, Route, Navigate, useNavigate } from "react-router-dom";

import AdminPage from "./AdminPage";
import StudentPage from "./StudentPage";
import Buttons from "./Buttons";
import AuthContent from "./AuthContent";
import LoginForm from "./LoginForm";
import WelcomeContent from "./WelcomeContent";
import { setAuthHeader, request, decodeToken } from "../helpers/axios-helper";

export default function AppContent() {
  const [role, setRole] = useState(null);
  const navigate = useNavigate();

  const login = () => {
    navigate("/login");
  };

  const logout = () => {
    setRole(null);
    setAuthHeader(null);
    navigate("/");
  };

  const onLogin = (e, username, password) => {
    e.preventDefault();
    request("POST", "/api/auth/login", { username, password })
      .then((response) => {
        const token = response.data.token;
        setAuthHeader(token);

        const decodedToken = decodeToken(token);
        const userRole = decodedToken?.role;

        setRole(userRole);

        if (userRole === "ROLE_ADMIN") {
          navigate("/admin");
        } else if (userRole === "ROLE_STUDENT") {
          navigate("/student");
        } else {
          navigate("/");
        }
      })
      .catch((error) => {
        console.error("Login failed", error);
        setAuthHeader(null);
        navigate("/");
      });
  };

  return (
    <>
      <Buttons login={login} logout={logout} />
      <Routes>
        <Route path="/" element={<WelcomeContent />} />
        <Route
          path="/login"
          element={<LoginForm onLogin={onLogin} />}
        />
        <Route
          path="/admin"
          element={role === "ROLE_ADMIN" ? <AdminPage onLogout={logout} /> : <Navigate to="/" />}
        />
        <Route
          path="/student"
          element={role === "ROLE_STUDENT" ? <StudentPage /> : <Navigate to="/" />}
        />
        <Route path="/messages" element={<AuthContent />} />
      </Routes>
    </>
  );
}

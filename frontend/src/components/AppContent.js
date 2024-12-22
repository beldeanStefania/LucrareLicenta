import React, { useState } from "react";
import { Routes, Route, useNavigate } from "react-router-dom";
import AdminPage from "./AdminPage";
import StudentPage from "./StudentPage";
import WelcomeContent from "./WelcomeContent";

export default function AppContent() {
  const [isLoggedIn, setIsLoggedIn] = useState(false); // Stare pentru autentificare
  const [username, setUsername] = useState(""); // Stare pentru username
  const navigate = useNavigate();

  const handleLoginClick = () => {
    const user = prompt("Enter username:");
    const pass = prompt("Enter password:");

    if (user === "admin1" && pass === "password") {
      setIsLoggedIn(true);
      setUsername(user); // Stochează username-ul
      navigate("/admin"); // Redirecționează către AdminPage
    } else {
      alert("Invalid credentials! Please try again.");
    }
  };

  const handleLogoutClick = () => {
    setIsLoggedIn(false); // Resetează autentificarea
    setUsername(""); // Șterge username-ul
    navigate("/"); // Redirecționează către WelcomeContent
  };

  return (
    <div className="welcome-container">
      <div className="header">
        <button
          className="btn btn-login"
          onClick={isLoggedIn ? handleLogoutClick : handleLoginClick}
        >
          {isLoggedIn ? "Logout" : "Login"}
        </button>
      </div>

      <h1 className="welcome-header">Welcome</h1>
      {isLoggedIn && <p>Logged in as: {username}</p>}

      <Routes>
        <Route path="/" element={<WelcomeContent />} />
        <Route
          path="/admin"
          element={isLoggedIn ? <AdminPage /> : <WelcomeContent />}
        />
        <Route
          path="/student"
          element={isLoggedIn ? <StudentPage /> : <WelcomeContent />}
        />
      </Routes>
    </div>
  );
}

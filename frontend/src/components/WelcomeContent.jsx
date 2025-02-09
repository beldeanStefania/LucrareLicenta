import React from "react";
import { useNavigate } from "react-router-dom";

export default function WelcomePage() {
  const navigate = useNavigate();

  return (
    <div className="welcome-page">
      <h1>Welcome to the Faculty Management System</h1>
      <p>Please log in to continue.</p>
      <div className="button-group">
        <button className="btn btn-primary" onClick={() => navigate("/login")}>Login</button>
      </div>
    </div>
  );
}
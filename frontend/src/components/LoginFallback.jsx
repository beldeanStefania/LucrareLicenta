import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { request, setAuthHeader } from "../helpers/axios-helper";
import { FaUser, FaLock, FaSignInAlt, FaUniversity } from "react-icons/fa";

export default function LoginFallback({ onLogin }) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleLogin = (e) => {
    e.preventDefault();
    setIsLoading(true);
    setError("");

    request("POST", "/api/auth/login", { username, password })
      .then((response) => {
        const token = response.data.token;
        console.log("Login response received");
        
        if (!token) {
          throw new Error("No token received from server");
        }
        
        setAuthHeader(token);
        localStorage.setItem("auth_token", token);
        
        navigate("/dashboard");
        if (onLogin) onLogin();
      })
      .catch((error) => {
        console.error("Failed to login:", error);
        setError("Invalid credentials. Please try again.");
      })
      .finally(() => {
        setIsLoading(false);
      });
  };
  
  return (
    <div className="login-page">
      <div className="login-container">
        <div className="login-logo">
          <FaUniversity size={80} color="#3498db" />
        </div>
        
        <h1 className="login-title">University Portal</h1>
        <p className="login-subtitle">Sign in to access your university dashboard</p>
        
        {error && (
          <div style={{ 
            padding: '10px',
            marginBottom: '15px',
            backgroundColor: 'rgba(231, 76, 60, 0.1)',
            color: '#e74c3c',
            borderRadius: '4px',
            fontSize: '14px'
          }}>
            {error}
          </div>
        )}
        
        <form onSubmit={handleLogin} className="login-form">
          <div className="form-group">
            <label htmlFor="username">
              <FaUser style={{ marginRight: '8px' }} />
              Username
            </label>
            <input
              id="username"
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
              placeholder="Enter your username"
              disabled={isLoading}
            />
          </div>
          <div className="form-group">
            <label htmlFor="password">
              <FaLock style={{ marginRight: '8px' }} />
              Password
            </label>
            <input
              id="password"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              placeholder="Enter your password"
              disabled={isLoading}
            />
          </div>
          <button 
            type="submit" 
            className="btn"
            disabled={isLoading}
          >
            {isLoading ? 'Signing in...' : (
              <>
                <FaSignInAlt style={{ marginRight: '8px' }} />
                Sign In
              </>
            )}
          </button>
        </form>
        
        <div className="login-footer">
          <p>© {new Date().getFullYear()} University Management System</p>
          <p>A secure platform for students, professors, and administrators</p>
        </div>
      </div>
    </div>
  );
}

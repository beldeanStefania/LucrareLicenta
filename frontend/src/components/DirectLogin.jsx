import React, { useState } from "react";
import axios from "axios";
import { FaUser, FaLock, FaSignInAlt, FaUniversity } from "react-icons/fa";
import "./LoginForm.css";

export default function DirectLogin() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState(false);

  const handleLogin = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setError("");
    
    try {
      // Use vanilla JavaScript fetch instead of axios
      console.log("Attempting login with credentials:", { username });
      
      // Clear any previous auth state
      localStorage.removeItem("auth_token");
      sessionStorage.clear();
      
      // Using fetch instead of axios to avoid any configuration issues
      const response = await fetch("http://176.34.129.151:8080/api/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Cache-Control": "no-cache, no-store, must-revalidate",
          "Pragma": "no-cache",
          "Expires": "0"
        },
        body: JSON.stringify({ username, password }),
        credentials: "include"
      });
      
      if (!response.ok) {
        throw new Error(`Login failed with status: ${response.status}`);
      }
      
      const data = await response.json();
      console.log("Login response data:", data);
      
      const token = data.token;
      const role = data.role;
      
      console.log("Login successful with role:", role);
      
      if (token) {
        // Store the token in localStorage
        localStorage.setItem("auth_token", token);
        
        // Set Authorization header for future requests
        axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
        
        setSuccess(true);
        
        // Determine which page to redirect to based on role
        let dashboardUrl = "/dashboard";
        if (role === "ROLE_ADMIN") {
          dashboardUrl = "/admin";
        } else if (role === "ROLE_STUDENT") {
          dashboardUrl = "/student";
        } else if (role === "ROLE_PROFESOR") {
          dashboardUrl = "/profesor";
        }
        
        // Clear any previous state to ensure a clean login
        sessionStorage.clear();
        
        // Force clear any cache for URLs we're about to navigate to
        if ('caches' in window) {
          caches.keys().then(names => {
            names.forEach(name => {
              caches.delete(name);
            });
          });
        }
        
        // Redirect to the dashboard after a short delay
        setTimeout(() => {
          // Use hard page navigation to avoid any routing or state issues
          window.location.replace(dashboardUrl);
        }, 1000);
      } else {
        throw new Error("No token received from server");
      }
    } catch (err) {
      console.error("Login error:", err);
      setError("Invalid credentials. Please try again.");
    } finally {
      setIsLoading(false);
    }
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
        
        {success && (
          <div style={{ 
            padding: '10px',
            marginBottom: '15px',
            backgroundColor: 'rgba(46, 204, 113, 0.1)',
            color: '#27ae60',
            borderRadius: '4px',
            fontSize: '14px'
          }}>
            Login successful! Redirecting to dashboard...
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
              disabled={isLoading || success}
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
              disabled={isLoading || success}
            />
          </div>
          <button 
            type="submit" 
            className="btn"
            disabled={isLoading || success}
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

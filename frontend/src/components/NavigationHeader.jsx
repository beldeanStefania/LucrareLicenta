import React, { useState, useEffect } from "react"; 
import { request } from "../helpers/axios-helper"; 
import { FaUserCircle, FaBars, FaTimes, FaUniversity } from "react-icons/fa";
import { NavLink, useNavigate } from "react-router-dom";
import PasswordChangeModal from "./PasswordChangeModal";
import "./NavigationHeader.css";

export default function NavigationHeader({ userRole, userName, onLogout }) {
  const [menuOpen, setMenuOpen] = useState(false);
  const [showChangePwd, setShowChangePwd] = useState(false);
  const [showDropdown, setShowDropdown] = useState(false);
  const [fetchedUsername, setFetchedUsername] = useState("");

  const navigate = useNavigate();

  const toggleMenu = () => setMenuOpen((open) => !open);

  const handleNavigate = (to) => {
    navigate(to);
    setMenuOpen(false);
  };

  const getRoleName = (role) => {
    switch (role) {
      case "ROLE_ADMIN":
        return "Administrator";
      case "ROLE_STUDENT":
        return "Student";
      case "ROLE_PROFESOR":
        return "Professor";
      default:
        return "User";
    }
  };

  useEffect(() => {
    request("GET", "/api/auth/userInfo")
      .then((res) => {
        setFetchedUsername(res.data.username);
      })
      .catch((err) => {
        console.error("Nu am putut obține username-ul:", err);
      });
  }, []);

  return (
    <header className="app-header">
      <div className="header-container">
        <div className="logo-section">
          <FaUniversity size={28} />
          <h1>University Management System</h1>
        </div>

        <button className="mobile-menu-toggle" onClick={toggleMenu}>
          {menuOpen ? <FaTimes size={24} /> : <FaBars size={24} />}
        </button>

        <nav className={`main-nav ${menuOpen ? "open" : ""}`}>
          <ul className="nav-links">
            {/* Dashboard */}
            <li className="nav-item">
              <NavLink
                to="/student"
                className={({ isActive }) =>
                  isActive ? "nav-link active" : "nav-link"
                }
                onClick={() => handleNavigate("/student")}
              >
                Dashboard
              </NavLink>
            </li>

            {userRole === "ROLE_STUDENT" && (
              <>
                {/* Schedule */}
                <li className="nav-item">
                  <NavLink
                    to={{ pathname: "/student", hash: "#schedule" }}
                    className={({ isActive }) =>
                      window.location.hash === "#schedule"
                        ? "nav-link active"
                        : "nav-link"
                    }
                    onClick={() =>
                      handleNavigate({
                        pathname: "/student",
                        hash: "#schedule",
                      })
                    }
                  >
                    Schedule
                  </NavLink>
                </li>
                {/* Grades */}
                <li className="nav-item">
                  <NavLink
                    to={{ pathname: "/student", hash: "#grades" }}
                    className={({ isActive }) =>
                      window.location.hash === "#grades"
                        ? "nav-link active"
                        : "nav-link"
                    }
                    onClick={() =>
                      handleNavigate({ pathname: "/student", hash: "#grades" })
                    }
                  >
                    Grades
                  </NavLink>
                </li>
              </>
            )}

            {userRole === "ROLE_ADMIN" && (
              <>
                <li className="nav-item">
                  <NavLink
                    to="/admin/students"
                    className="nav-link"
                    onClick={() => handleNavigate("/admin/students")}
                  >
                    Students
                  </NavLink>
                </li>
                <li className="nav-item">
                  <NavLink
                    to="/admin/professors"
                    className="nav-link"
                    onClick={() => handleNavigate("/admin/professors")}
                  >
                    Professors
                  </NavLink>
                </li>
              </>
            )}

            {userRole === "ROLE_PROFESOR" && (
              <>
                <li className="nav-item">
                  <NavLink
                    to={{ pathname: "/student", hash: "#schedule" }}
                    className="nav-link"
                    onClick={() =>
                      handleNavigate({
                        pathname: "/student",
                        hash: "#schedule",
                      })
                    }
                  >
                    My Schedule
                  </NavLink>
                </li>
                <li className="nav-item">
                  <NavLink
                    to={{ pathname: "/student", hash: "#grades" }}
                    className="nav-link"
                    onClick={() =>
                      handleNavigate({ pathname: "/student", hash: "#grades" })
                    }
                  >
                    Manage Grades
                  </NavLink>
                </li>
                <li className="nav-item">
                  <NavLink
                    to="/professor/rooms"
                    className="nav-link"
                    onClick={() => handleNavigate("/professor/rooms")}
                  >
                    Reserve Rooms
                  </NavLink>
                </li>
              </>
            )}
          </ul>

          <div className="user-section">
            <div
              className="user-info"
              onClick={() => setShowDropdown((d) => !d)}
            >
              <FaUserCircle size={24} />
              <div className="user-details">
                {}
                <span className="user-name">
                  {fetchedUsername || userName || "User"}
                </span>
                <span className="user-role">{getRoleName(userRole)}</span>
              </div>
            </div>
            {showDropdown && (
              <div className="user-dropdown-menu">
                <button
                  onClick={() => {
                    setShowChangePwd(true);
                    setShowDropdown(false);
                  }}
                >
                  Schimbă parola
                </button>
                <button onClick={onLogout}>Logout</button>
              </div>
            )}
          </div>
        </nav>
      </div>

      {showChangePwd && (
        <PasswordChangeModal
          username={fetchedUsername || userName}
          onClose={() => setShowChangePwd(false)}
        />
      )}
    </header>
  );
}

import React, { useState } from 'react';
import { FaUserCircle, FaSignOutAlt, FaUniversity, FaBars, FaTimes } from 'react-icons/fa';
import './NavigationHeader.css';

export default function NavigationHeader({ userRole, userName, onLogout }) {
  const [menuOpen, setMenuOpen] = useState(false);
  
  const toggleMenu = () => {
    setMenuOpen(!menuOpen);
  };

  const getRoleName = (role) => {
    switch(role) {
      case 'ROLE_ADMIN':
        return 'Administrator';
      case 'ROLE_STUDENT':
        return 'Student';
      case 'ROLE_PROFESOR':
        return 'Professor';
      default:
        return 'User';
    }
  };

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

        <nav className={`main-nav ${menuOpen ? 'open' : ''}`}>
          <ul className="nav-links">
            <li className="nav-item">
              <a href="#dashboard" className="nav-link active">Dashboard</a>
            </li>
            {userRole === 'ROLE_ADMIN' && (
              <>
                <li className="nav-item">
                  <a href="#students" className="nav-link">Students</a>
                </li>
                <li className="nav-item">
                  <a href="#professors" className="nav-link">Professors</a>
                </li>
              </>
            )}
            {userRole === 'ROLE_STUDENT' && (
              <>
                <li className="nav-item">
                  <a href="#schedule" className="nav-link">Schedule</a>
                </li>
                <li className="nav-item">
                  <a href="#grades" className="nav-link">Grades</a>
                </li>
              </>
            )}
            {userRole === 'ROLE_PROFESOR' && (
              <>
                <li className="nav-item">
                  <a href="#schedule" className="nav-link">My Schedule</a>
                </li>
                <li className="nav-item">
                  <a href="#grades" className="nav-link">Manage Grades</a>
                </li>
                <li className="nav-item">
                  <a href="#rooms" className="nav-link">Reserve Rooms</a>
                </li>
              </>
            )}
          </ul>
          
          <div className="user-section">
            <div className="user-info">
              <FaUserCircle size={24} />
              <div className="user-details">
                <span className="user-name">{userName || 'User'}</span>
                <span className="user-role">{getRoleName(userRole)}</span>
              </div>
            </div>
            <button className="logout-button" onClick={onLogout}>
              <FaSignOutAlt />
              <span>Logout</span>
            </button>
          </div>
        </nav>
      </div>
    </header>
  );
}

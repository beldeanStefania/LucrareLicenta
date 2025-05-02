// NavigationHeader.jsx
import React, { useState } from 'react';
import { FaUserCircle, FaSignOutAlt, FaUniversity, FaBars, FaTimes } from 'react-icons/fa';
import { NavLink, useNavigate } from 'react-router-dom';
import './NavigationHeader.css';

export default function NavigationHeader({ userRole, userName, onLogout }) {
  const [menuOpen, setMenuOpen] = useState(false);
  const navigate = useNavigate();

  const toggleMenu = () => setMenuOpen(open => !open);

  const handleNavigate = (to) => {
    navigate(to);
    setMenuOpen(false);
  };

  const getRoleName = (role) => {
    switch(role) {
      case 'ROLE_ADMIN':     return 'Administrator';
      case 'ROLE_STUDENT':   return 'Student';
      case 'ROLE_PROFESOR':  return 'Professor';
      default:               return 'User';
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
          {menuOpen ? <FaTimes size={24}/> : <FaBars size={24}/>}
        </button>

        <nav className={`main-nav ${menuOpen ? 'open' : ''}`}>
          <ul className="nav-links">
            {/* Dashboard */}
            <li className="nav-item">
              <NavLink
                to="/student"
                className={({ isActive }) => isActive ? 'nav-link active' : 'nav-link'}
                onClick={() => handleNavigate('/student')}
              >
                Dashboard
              </NavLink>
            </li>

            {userRole === 'ROLE_STUDENT' && (
              <>
                {/* Schedule */}
                <li className="nav-item">
                  <NavLink
                    to={{ pathname: '/student', hash: '#schedule' }}
                    className={({ isActive }) => window.location.hash === '#schedule' ? 'nav-link active' : 'nav-link'}
                    onClick={() => handleNavigate({ pathname: '/student', hash: '#schedule' })}
                  >
                    Schedule
                  </NavLink>
                </li>
                {/* Grades */}
                <li className="nav-item">
                  <NavLink
                    to={{ pathname: '/student', hash: '#grades' }}
                    className={({ isActive }) => window.location.hash === '#grades' ? 'nav-link active' : 'nav-link'}
                    onClick={() => handleNavigate({ pathname: '/student', hash: '#grades' })}
                  >
                    Grades
                  </NavLink>
                </li>
              </>
            )}

            {userRole === 'ROLE_ADMIN' && (
              <>
                <li className="nav-item">
                  <NavLink
                    to="/admin/students"
                    className="nav-link"
                    onClick={() => handleNavigate('/admin/students')}
                  >
                    Students
                  </NavLink>
                </li>
                <li className="nav-item">
                  <NavLink
                    to="/admin/professors"
                    className="nav-link"
                    onClick={() => handleNavigate('/admin/professors')}
                  >
                    Professors
                  </NavLink>
                </li>
              </>
            )}

            {userRole === 'ROLE_PROFESOR' && (
              <>
                <li className="nav-item">
                  <NavLink
                    to={{ pathname: '/student', hash: '#schedule' }}
                    className="nav-link"
                    onClick={() => handleNavigate({ pathname: '/student', hash: '#schedule' })}
                  >
                    My Schedule
                  </NavLink>
                </li>
                <li className="nav-item">
                  <NavLink
                    to={{ pathname: '/student', hash: '#grades' }}
                    className="nav-link"
                    onClick={() => handleNavigate({ pathname: '/student', hash: '#grades' })}
                  >
                    Manage Grades
                  </NavLink>
                </li>
                <li className="nav-item">
                  <NavLink
                    to="/professor/rooms"
                    className="nav-link"
                    onClick={() => handleNavigate('/professor/rooms')}
                  >
                    Reserve Rooms
                  </NavLink>
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

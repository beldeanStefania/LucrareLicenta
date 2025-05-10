// src/components/StudentPage.jsx
import React, { useEffect, useState } from "react";
import { request } from "../helpers/axios-helper";
import axios from "axios";
import { useNavigate, useLocation } from "react-router-dom";
import NavigationHeader from "./NavigationHeader";
import { 
  FaGraduationCap, FaBook, FaCalendarAlt, 
  FaRegFileAlt 
} from "react-icons/fa";
import "./StudentPage.css";

export default function StudentPage({ onLogout }) {
  const [userData, setUserData] = useState(null);
  const [student, setStudent] = useState(null);
  const [schedule, setSchedule] = useState([]);
  const [grades, setGrades] = useState([]);
  const [loading, setLoading] = useState({
    user: true,
    schedule: true,
    grades: true,
  });

  const navigate = useNavigate();
  const location = useLocation();

  // 1) Preluăm userInfo + detalii student
  useEffect(() => {
    request("GET", "/api/auth/userInfo")
      .then(res => {
        // destructurăm tot ce ne trebuie
        const { username, role, cod, an, grupa } = res.data;
        setUserData({ username, role });
        setStudent({ cod, an, grupa });
  
        // încărcăm restul pe baza codului și grupei
        fetchGrades(cod);
        fetchSchedule(grupa);
      })
      .catch(() => {
        // chiar dacă e eroare, scoatem spinnerul
      })
      .finally(() => {
        setLoading(l => ({ ...l, user: false }));
      });
  }, []);
  

  // 2) Încarcă orarul
  const fetchSchedule = grp => {
    request("GET", `/api/orare/getAll/${grp}`)
      .then(res => setSchedule(res.data || []))
      .catch(() => setSchedule([]))
      .finally(() => setLoading(l => ({ ...l, schedule: false })));
  };

  // 3) Încarcă notele
  const fetchGrades = cod => {
    request("GET", `/api/catalogStudentMaterie/getNote/${cod}`)
      .then(res => setGrades(res.data || []))
      .catch(() => setGrades([]))
      .finally(() => setLoading(l => ({ ...l, grades: false })));
  };

  // 4) Calcul medie ponderată
  const getAverageGrade = () => {
    if (!grades.length) return 0;
    const totalCred = grades.reduce((sum, g) => sum + (g.credite || 0), 0);
    if (!totalCred) return 0;
    const weighted = grades.reduce((sum, g) => sum + g.nota * (g.credite || 0), 0);
    return (weighted / totalCred).toFixed(2);
  };

  // 5) Număr clase azi
  const getUpcomingClassesCount = () => {
    const days = ['Duminica','Luni','Marti','Miercuri','Joi','Vineri','Sambata'];
    return schedule.filter(i => i.zi === days[new Date().getDay()]).length;
  };

  // 6) Scroll la fragment/hash
  useEffect(() => {
    if (location.hash) {
      const el = document.getElementById(location.hash.slice(1));
      if (el) el.scrollIntoView({ behavior: "smooth", block: "start" });
    } else {
      window.scrollTo({ top: 0, behavior: "smooth" });
    }
  }, [location]);

  // 7) Navigare către ContractSelectionPage
  const goToContractSelection = () => {
    if (!student) return;
    navigate(`/contract/select?cod=${student.cod}&an=${student.an}`);
  };

  // 8) Spinner inițial
  if (loading.user) {
    return (
      <div className="loading-container">
        <div className="loading-spinner"></div>
        <div className="loading-text">Loading student information...</div>
      </div>
    );
  }

  // 9) UI
  return (
    <div className="student-page">
      <NavigationHeader 
        userRole="ROLE_STUDENT" 
        userName={userData.username}
        onLogout={onLogout}
      />

      <div className="student-content">
        {/* Dashboard */}
        <div className="dashboard-welcome">
          <h1 className="welcome-title">Welcome, {userData.username}!</h1>
          <p className="welcome-subtitle">
            Here's an overview of your academic status
          </p>
        </div>
        <div className="dashboard-grid">
          <div className="stat-card">
            <div className="stat-icon"><FaGraduationCap size={24}/></div>
            <div className="stat-value">{getAverageGrade()}</div>
            <div className="stat-label">Average Grade</div>
          </div>
          <div className="stat-card">
            <div className="stat-icon"><FaBook size={24}/></div>
            <div className="stat-value">{grades.length}</div>
            <div className="stat-label">Total Courses</div>
          </div>
          <div className="stat-card">
            <div className="stat-icon"><FaCalendarAlt size={24}/></div>
            <div className="stat-value">{getUpcomingClassesCount()}</div>
            <div className="stat-label">Today's Classes</div>
          </div>
        </div>

        {/* Grades Section */}
        <div id="grades" className="section-container">
          <div className="section-header">
            <FaRegFileAlt style={{ marginRight: '10px' }}/>
            <h2>Your Grades</h2>
          </div>
          <div className="section-content">
            {loading.grades ? (
              <div className="loading-container">
                <div className="loading-spinner"/><p>Loading grades...</p>
              </div>
            ) : grades.length ? (
              <table className="data-table">
                <thead>
                  <tr>
                    <th>Course</th><th>Code</th>
                    <th>Grade</th><th>Semester</th><th>Status</th>
                  </tr>
                </thead>
                <tbody>
                  {grades.map((g,i) => (
                    <tr key={i}>
                      <td>{g.numeMaterie}</td>
                      <td>{g.codMaterie}</td>
                      <td className={g.nota>=5?'grade-passing':'grade-failing'}>
                        {g.nota}
                      </td>
                      <td>{g.semestru}</td>
                      <td>
                        {g.nota>=5
                          ? <span className="grade-passing">Passed</span>
                          : <span className="grade-failing">Failed</span>
                        }
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            ) : (
              <div className="empty-state">
                <FaRegFileAlt className="empty-state-icon"/>
                <div className="empty-state-text">No grades available</div>
              </div>
            )}
          </div>
        </div>

        {/* (Schedule Section rămâne neschimbat) */}

        {/* Buton Generare Contract */}
        <div style={{ textAlign: 'center', margin: '40px 0' }}>
          <button
            onClick={goToContractSelection}
            className="btn-generate"
          >
            Generează Contractul de Studii pentru anul {student.an}
          </button>
        </div>
      </div>
    </div>
  );
}

// StudentPage.jsx
import React, { useEffect, useState } from "react";
import { request } from "../helpers/axios-helper";
import { useNavigate, useLocation } from "react-router-dom";
import NavigationHeader from "./NavigationHeader";
import { 
  FaGraduationCap, FaBook, FaCalendarAlt, 
  FaRegFileAlt, FaClock, FaMapMarkerAlt, 
  FaChalkboardTeacher 
} from "react-icons/fa";
import "./StudentPage.css";

export default function StudentPage({ onLogout }) {
  const [userData, setUserData] = useState(null);
  const [schedule, setSchedule] = useState([]);
  const [grades, setGrades] = useState([]);
  const [loading, setLoading] = useState({
    user: true,
    schedule: true,
    grades: true
  });

  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    request("GET", "/api/auth/userInfo")
      .then(res => {
        setUserData(res.data);
        setLoading(l => ({ ...l, user: false }));
        fetchGrades(res.data.cod);
        fetchSchedule(res.data.grupa);
      })
      .catch(() => setLoading(l => ({ ...l, user: false })));
  }, []);

  const fetchSchedule = grp => {
    request("GET", `/api/orare/getAll/${grp}`)
      .then(res => setSchedule(Array.isArray(res.data) ? res.data : []))
      .catch(() => setSchedule([]))
      .finally(() => setLoading(l => ({ ...l, schedule: false })));
  };

  const fetchGrades = cod => {
    request("GET", `/api/catalogStudentMaterie/getNote/${cod}`)
      .then(res => setGrades(Array.isArray(res.data) ? res.data : []))
      .catch(() => setGrades([]))
      .finally(() => setLoading(l => ({ ...l, grades: false })));
  };

  const getAverageGrade = () => {
    if (!grades.length) return 0;
    const totalCred = grades.reduce((sum, g) => sum + (g.credite||0), 0);
    if (!totalCred) return 0;
    const weighted = grades.reduce((sum, g) => sum + g.nota*(g.credite||0), 0);
    return (weighted/totalCred).toFixed(2);
  };

  const getUpcomingClassesCount = () => {
    const dayNames = ['Duminica','Luni','Marti','Miercuri','Joi','Vineri','Sambata'];
    return schedule.filter(i => i.zi === dayNames[new Date().getDay()]).length;
  };

  // scroll pe hash sau top
  useEffect(() => {
    if (location.hash) {
      const id = location.hash.slice(1);
      const el = document.getElementById(id);
      if (el) el.scrollIntoView({ behavior: "smooth", block: "start" });
    } else {
      window.scrollTo({ top: 0, behavior: "smooth" });
    }
  }, [location]);

  const goToContractSelection = () => {
    const sem = window.prompt("Introdu semestrul pentru care vrei să generezi contractul:");
    if (!sem || isNaN(sem)) {
      alert("Te rog să introduci un număr valid pentru semestru.");
      return;
    }
    navigate(`/contract/select?cod=${userData.cod}&an=${userData.an}&semestru=${sem}`);
  };

  if (loading.user) {
    return (
      <div className="loading-container">
        <div className="loading-spinner"></div>
        <div className="loading-text">Loading student information...</div>
      </div>
    );
  }

  return (
    <div className="student-page">
      <NavigationHeader 
        userRole="ROLE_STUDENT" 
        userName={userData.username}
        onLogout={onLogout}
      />
      <div className="student-content">
        <div className="dashboard-welcome">
          <h1 className="welcome-title">Welcome, {userData.username}!</h1>
          <p className="welcome-subtitle">Here's an overview of your academic status</p>
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
                  <tr><th>Course</th><th>Code</th><th>Grade</th><th>Semester</th><th>Status</th></tr>
                </thead>
                <tbody>
                  {grades.map((g,i) => (
                    <tr key={i}>
                      <td>{g.numeMaterie}</td>
                      <td>{g.codMaterie}</td>
                      <td className={g.nota>=5?'grade-passing':'grade-failing'}>{g.nota}</td>
                      <td>{g.semestru}</td>
                      <td>{g.nota>=5
                        ? <span className="grade-passing">Passed</span>
                        : <span className="grade-failing">Failed</span>
                      }</td>
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

        <div id="schedule" className="section-container">
          <div className="section-header">
            <FaCalendarAlt style={{ marginRight: '10px' }}/>
            <h2>Class Schedule</h2>
          </div>
          <div className="section-content">
            {loading.schedule ? (
              <div className="loading-container">
                <div className="loading-spinner"/><p>Loading schedule...</p>
              </div>
            ) : schedule.length ? (
              <table className="data-table">
                <thead>
                  <tr><th>Ziua</th><th>Orele</th><th>Frecvența</th><th>Sala</th><th>Tipul</th><th>Profesor</th><th>Disciplina</th></tr>
                </thead>
                <tbody>
                  {schedule.map((it,i) => (
                    <tr key={i} className={
                      it.tipul==='Curs'? 'schedule-item-course':
                      it.tipul==='Laborator'? 'schedule-item-lab':
                      'schedule-item-seminar'
                    }>
                      <td>{it.zi}</td>
                      <td>{`${it.oraInceput}:00 - ${it.oraSfarsit}:00`}</td>
                      <td>{it.disciplina}</td><td>{it.sala}</td><td>{it.tipul}</td><td>{it.cadruDidactic}</td><td>{it.frecventa==='saptamanal'?'Weekly':it.frecventa}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            ) : (
              <div className="empty-state">
                <FaCalendarAlt className="empty-state-icon"/>
                <div className="empty-state-text">No schedule available</div>
              </div>
            )}
          </div>
        </div>

        {/* Buton Generare Contract */}
        <div style={{ textAlign: 'center', marginTop: '40px', marginBottom: '20px' }}>
          <button
            onClick={goToContractSelection}
            style={{
              padding: '12px 24px',
              backgroundColor: 'var(--secondary-color)',
              color: '#fff',
              border: 'none',
              borderRadius: '8px',
              cursor: 'pointer',
              fontWeight: 'bold',
              fontSize: '16px',
              boxShadow: '0 2px 8px rgba(0, 0, 0, 0.15)',
              transition: 'all 0.3s ease'
            }}
            onMouseOver={e => {
              e.currentTarget.style.transform = "translateY(-2px)";
              e.currentTarget.style.boxShadow = "0 4px 12px rgba(0, 0, 0, 0.2)";
            }}
            onMouseOut={e => {
              e.currentTarget.style.transform = "translateY(0)";
              e.currentTarget.style.boxShadow = "0 2px 8px rgba(0, 0, 0, 0.15)";
            }}
          >
            Generează Contractul de Studii
          </button>
        </div>
      </div>
    </div>
  );
}

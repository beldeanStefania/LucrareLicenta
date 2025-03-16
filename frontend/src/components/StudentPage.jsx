import React, { useEffect, useState } from "react";
import { request } from "../helpers/axios-helper";
import NavigationHeader from "./NavigationHeader";
import { 
  FaGraduationCap, FaBook, FaCalendarAlt, FaChalkboardTeacher, 
  FaRegFileAlt, FaClock, FaMapMarkerAlt 
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

  useEffect(() => {
    fetchUserInfo();
  }, []);

  const fetchUserInfo = () => {
    request("GET", "/api/auth/userInfo")
      .then((response) => {
        setUserData(response.data);
        setLoading(prev => ({ ...prev, user: false }));
        
        // After we have user info, fetch grades and schedule
        fetchGrades(response.data.cod);
        fetchSchedule(response.data.grupa);
      })
      .catch((error) => {
        console.error("Failed to fetch user info:", error);
        setLoading(prev => ({ ...prev, user: false }));
      });
  };

  const fetchSchedule = (userGroup) => {
    request("GET", `/api/orare/getAll/${userGroup}`)
      .then((response) => {
        if (Array.isArray(response.data)) {
          setSchedule(response.data);
        } else {
          setSchedule([]);
          console.error("Invalid schedule data format:", response.data);
        }
      })
      .catch((error) => {
        console.error("Failed to fetch schedule:", error);
        setSchedule([]);
      })
      .finally(() => {
        setLoading(prev => ({ ...prev, schedule: false }));
      });
  };

  const fetchGrades = (studentCode) => {
    request("GET", `/api/catalogStudentMaterie/getNote/${studentCode}`)
      .then((response) => {
        if (Array.isArray(response.data)) {
          setGrades(response.data);
        } else {
          setGrades([]);
          console.error("Invalid grades data format:", response.data);
        }
      })
      .catch((error) => {
        console.error("Failed to fetch grades:", error);
        setGrades([]);
      })
      .finally(() => {
        setLoading(prev => ({ ...prev, grades: false }));
      });
  };
  
  // Calculate statistics for dashboard
  const getPassingGradesCount = () => {
    return grades.filter(grade => grade.nota >= 5).length;
  };
  
  const getAverageGrade = () => {
    if (grades.length === 0) return 0;
    const sum = grades.reduce((total, grade) => total + grade.nota, 0);
    return (sum / grades.length).toFixed(2);
  };
  
  const getUpcomingClassesCount = () => {
    const today = new Date();
    const dayNames = ['Duminica', 'Luni', 'Marti', 'Miercuri', 'Joi', 'Vineri', 'Sambata'];
    const todayName = dayNames[today.getDay()];
    
    return schedule.filter(item => item.zi === todayName).length;
  };

  // Loading state
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
        onLogout={onLogout}/>

      <div className="student-content">
        {/* Welcome section */}
        <div className="dashboard-welcome">
          <h1 className="welcome-title">Welcome, {userData ? userData.username : 'Student'}!</h1>
          <p className="welcome-subtitle">
            Here's an overview of your academic status
          </p>
        </div>

        {/* Stats dashboard */}
        <div className="dashboard-grid">
          <div className="stat-card">
            <div className="stat-icon">
              <FaGraduationCap size={24} />
            </div>
            <div className="stat-value">{getAverageGrade()}</div>
            <div className="stat-label">Average Grade</div>
          </div>
          
          <div className="stat-card">
            <div className="stat-icon">
              <FaBook size={24} />
            </div>
            <div className="stat-value">{grades.length}</div>
            <div className="stat-label">Total Courses</div>
          </div>
          
          <div className="stat-card">
            <div className="stat-icon">
              <FaCalendarAlt size={24} />
            </div>
            <div className="stat-value">{getUpcomingClassesCount()}</div>
            <div className="stat-label">Today's Classes</div>
          </div>
        </div>

        {/* Grades section */}
        <div id="grades" className="section-container">
          <div className="section-header">
            <h2>
              <FaRegFileAlt style={{ marginRight: '10px' }} />
              Your Grades
            </h2>
          </div>
          <div className="section-content">
            {loading.grades ? (
              <div className="loading-container">
                <div className="loading-spinner"></div>
                <p>Loading grades...</p>
              </div>
            ) : grades.length > 0 ? (
              <table className="data-table">
                <thead>
                  <tr>
                    <th>Course</th>
                    <th>Code</th>
                    <th>Grade</th>
                    <th>Semester</th>
                    <th>Status</th>
                  </tr>
                </thead>
                <tbody>
                  {grades.map((grade, index) => (
                    <tr key={index}>
                      <td>{grade.numeMaterie}</td>
                      <td>{grade.codMaterie}</td>
                      <td className={grade.nota >= 5 ? 'grade-passing' : 'grade-failing'}>
                        {grade.nota}
                      </td>
                      <td>{grade.semestru}</td>
                      <td>
                        {grade.nota >= 5 ? (
                          <span className="grade-passing">Passed</span>
                        ) : (
                          <span className="grade-failing">Failed</span>
                        )}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            ) : (
              <div className="empty-state">
                <div className="empty-state-icon">
                  <FaRegFileAlt />
                </div>
                <div className="empty-state-text">No grades available</div>
                <div className="empty-state-subtext">Grades will appear here once they are assigned by professors</div>
              </div>
            )}
          </div>
        </div>

        {/* Schedule section */}
        <div id="schedule" className="section-container">
          <div className="section-header">
            <h2>
              <FaCalendarAlt style={{ marginRight: '10px' }} />
              Class Schedule
            </h2>
          </div>
          <div className="section-content">
            {loading.schedule ? (
              <div className="loading-container">
                <div className="loading-spinner"></div>
                <p>Loading schedule...</p>
              </div>
            ) : schedule.length > 0 ? (
              <table className="data-table">
                <thead>
                  <tr>
                    <th>Ziua</th>
                    <th>Orele</th>
                    <th>Frecvența</th>
                    <th>Sala</th>
                    <th>Tipul</th>
                    <th>Formația</th>
                    <th>Disciplina</th>
                  </tr>
                </thead>
                <tbody>
                  {schedule.map((item, index) => {
                    // Determine class for type-based highlighting
                    let itemClass = '';
                    if (item.tipul === 'Curs') itemClass = 'schedule-item-course';
                    else if (item.tipul === 'Laborator') itemClass = 'schedule-item-lab';
                    else if (item.tipul === 'Seminar') itemClass = 'schedule-item-seminar';
                    
                    return (
                      <tr key={index} className={itemClass}>
                        <td>
                          <span style={{ display: 'flex', alignItems: 'center' }}>
                            <FaCalendarAlt style={{ marginRight: '8px', opacity: 0.7 }} />
                            {item.zi}
                          </span>
                        </td>
                        <td>
                          <span style={{ display: 'flex', alignItems: 'center' }}>
                            <FaClock style={{ marginRight: '8px', opacity: 0.7 }} />
                            {`${item.oraInceput}:00 - ${item.oraSfarsit}:00`}
                          </span>
                        </td>
                        <td>{item.disciplina}</td>
                        <td>{item.tipul}</td>
                        <td>
                          <span style={{ display: 'flex', alignItems: 'center' }}>
                            <FaMapMarkerAlt style={{ marginRight: '8px', opacity: 0.7 }} />
                            {item.sala}
                          </span>
                        </td>
                        <td>
                          <span style={{ display: 'flex', alignItems: 'center' }}>
                            <FaChalkboardTeacher style={{ marginRight: '8px', opacity: 0.7 }} />
                            {item.cadruDidactic}
                          </span>
                        </td>
                        <td>{item.frecventa === "saptamanal" ? "Weekly" : item.frecventa}</td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            ) : (
              <div className="empty-state">
                <div className="empty-state-icon">
                  <FaCalendarAlt />
                </div>
                <div className="empty-state-text">No schedule available</div>
                <div className="empty-state-subtext">Your class schedule will appear here once it's published</div>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

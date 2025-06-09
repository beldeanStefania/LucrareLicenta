
import React, { useEffect, useState } from "react";
import { request } from "../helpers/axios-helper";
import { useNavigate, useLocation } from "react-router-dom";
import NavigationHeader from "./NavigationHeader";
import ChatWidget from './ChatWidget';
import { 
  FaGraduationCap, FaBook, FaCalendarAlt, 
  FaRegFileAlt, FaClock, FaMapMarkerAlt, FaChalkboardTeacher 
} from "react-icons/fa";
//import "./StudentPage.css";
import "../styles/pages.css";

export default function StudentPage({ onLogout }) {
  const [todos, setTodos] = useState([]);
  const [newTitle, setNewTitle] = useState("");
  const [newDescription, setNewDescription] = useState("");
  const [newDeadline, setNewDeadline] = useState("");  // în format "YYYY-MM-DD"
  const [loadingTodos, setLoadingTodos] = useState(true);


  const [userData, setUserData] = useState(null);
  const [student, setStudent] = useState(null);
  const [schedule, setSchedule] = useState([]);
  const [grades, setGrades] = useState([]);
  const [loading, setLoading] = useState({
    user: true,
    schedule: true,
    grades: true,
  });

  const [selectedYear, setSelectedYear] = useState("");
  const [selectedSem, setSelectedSem] = useState("");

  const navigate = useNavigate();
  const location = useLocation();

const fetchTodos = (username) => {
  setLoadingTodos(true);
  request("GET", `/api/todo/user/${username}`)
    .then(res => {
      console.log("TODO response:", res.data); // 👈 Adaugă asta
      setTodos(res.data || []);
    })
    .catch(err => {
      console.error("Eroare la fetchTodos:", err);
      setTodos([]);
    })
    .finally(() => setLoadingTodos(false));
};

useEffect(() => {
  if (userData?.username) {
    fetchTodos(userData.username);
  }
}, [userData]);


const handleAddTodo = () => {
  if (!newTitle || !newDeadline) return;

  const payload = {
    username: userData.username,  // schimbare aici
    title: newTitle,
    description: newDescription,
    deadline: newDeadline
  };

  request("POST", "/api/todo/create", payload)
    .then(() => {
      setNewTitle("");
      setNewDescription("");
      setNewDeadline("");
      fetchTodos(userData.username);  // schimbare aici
    })
    .catch(err => {
      console.error("Eroare la adăugare todo:", err);
      alert("Eroare la crearea To-Do-ului!");
    });
};



  const handleMarkDone = (todoId) => {
  const todo = todos.find(t => t.id === todoId);
  if (!todo) return;

  const updatedTodo = {
    title: todo.title,
    description: todo.description,
    deadline: todo.deadline,
    done: true
  };

  request("PUT", `/api/todo/update/${todoId}`, updatedTodo)
    .then(() => fetchTodos(userData.username))  // schimbare aici
    .catch(err => console.error("Eroare la marcare todo ca done:", err));
};


  // 4. Funcția șterge un todo
  const handleDeleteTodo = (todoId) => {
    request("DELETE", `/api/todo/delete/${todoId}`)
      .then(res => {
        fetchTodos(userData.username);  // schimbare aici
      })
      .catch(err => console.error(err));
  };


 useEffect(() => {
  if (userData?.username) {
    fetchTodos(userData.username);  // ✔️ corect
  }
}, [userData]);



  useEffect(() => {
    request("GET", "/api/auth/userInfo")
      .then(res => {
        const { username, role, cod, an, grupa } = res.data;
        setUserData({ username, role });
        setStudent({ cod, an, grupa });
        fetchGrades(cod);
        fetchSchedule(grupa);
      })
      .catch(() => {})
      .finally(() => {
        setLoading(l => ({ ...l, user: false }));
      });
  }, []);

  const fetchSchedule = grp => {
    request("GET", `/api/orare/getAll/${grp}`)
      .then(res => setSchedule(res.data || []))
      .catch(() => setSchedule([]))
      .finally(() => setLoading(l => ({ ...l, schedule: false })));
  };

  const fetchGrades = cod => {
    request("GET", `/api/catalogStudentMaterie/getNote/${cod}`)
      .then(res => setGrades(res.data || []))
      .catch(() => setGrades([]))
      .finally(() => setLoading(l => ({ ...l, grades: false })));
  };

  const getAverageGrade = () => {
    const validGrades = grades.filter(g => g.nota != null);
    if (!validGrades.length) return 0;
    const totalCred = validGrades.reduce((sum, g) => sum + (g.credite || 0), 0);
    const weighted = validGrades.reduce((sum, g) => sum + g.nota * (g.credite || 0), 0);
    return (weighted / totalCred).toFixed(2);
  };

  const getUpcomingClassesCount = () => {
    const days = ['Duminica','Luni','Marti','Miercuri','Joi','Vineri','Sambata'];
    return schedule.filter(i => i.zi === days[new Date().getDay()]).length;
  };

  useEffect(() => {
    if (location.hash) {
      const el = document.getElementById(location.hash.slice(1));
      if (el) el.scrollIntoView({ behavior: "smooth", block: "start" });
    } else {
      window.scrollTo({ top: 0, behavior: "smooth" });
    }
  }, [location]);

  const goToContractSelection = () => {
    if (!student) return;
    navigate(`/contract/select?cod=${student.cod}&an=${student.an}`);
  };

  const uniqueYears = [1, 2, 3];
  const uniqueSems = [1, 2, 3, 4, 5, 6];

  const filteredGrades = grades.filter(g => {
  const byYear = selectedYear ? Math.ceil(g.semestru / 2) === Number(selectedYear) : true;
  const bySem = selectedSem ? g.semestru === Number(selectedSem) : true;
  return byYear && bySem;
});


  if (!student) {
    return (
      <div className="loading-container">
        <div className="loading-spinner"></div>
        <div className="loading-text">Loading student data...</div>
      </div>
    );
  }

 return (
  <div className="student-page">
    <NavigationHeader 
      userRole="ROLE_STUDENT" 
      userName={userData.username || ""}
      onLogout={onLogout}
    />

    <div className="student-content">
      <div className="dashboard-welcome">
        <p className="welcome-subtitle">
          Here's an overview of your academic status
        </p>
      </div>
      <div className="dashboard-grid">
        <div className="stat-card">
          <div className="stat-icon"><FaGraduationCap size={24} /></div>
          <div className="stat-value">{getAverageGrade()}</div>
          <div className="stat-label">Average Grade</div>
        </div>
        <div className="stat-card">
          <div className="stat-icon"><FaBook size={24} /></div>
          <div className="stat-value">{filteredGrades.length}</div>
          <div className="stat-label">Filtered Courses</div>
        </div>
        <div className="stat-card">
          <div className="stat-icon"><FaCalendarAlt size={24} /></div>
          <div className="stat-value">{getUpcomingClassesCount()}</div>
          <div className="stat-label">Today's Classes</div>
        </div>
      </div>

      <div id="grades" className="section-container">
        <div className="section-header">
          <FaRegFileAlt style={{ marginRight: '10px' }} />
          <h2>Your Grades</h2>
        </div>

        <div className="filter-bar">
          <label>
            An:
            <select value={selectedYear} onChange={e => setSelectedYear(e.target.value)}>
              <option value="">Toate</option>
              {uniqueYears.map(y => <option key={y} value={y}>{y}</option>)}
            </select>
          </label>
          <label>
            Semestru:
            <select value={selectedSem} onChange={e => setSelectedSem(e.target.value)}>
              <option value="">Toate</option>
              {uniqueSems.map(s => <option key={s} value={s}>{s}</option>)}
            </select>
          </label>
        </div>

        <div className="section-content">
          {loading.grades ? (
            <div className="loading-container">
              <div className="loading-spinner" /><p>Loading grades...</p>
            </div>
          ) : filteredGrades.length ? (
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
                {filteredGrades.map((g, i) => (
                  <tr key={i}>
                    <td>{g.numeMaterie}</td>
                    <td>{g.codMaterie}</td>
                    <td>{g.nota != null ? g.nota : ''}</td>
                    <td>{g.semestru}</td>
                    <td>
                      {g.nota != null
                        ? (g.nota >= 5
                            ? <span className="grade-passing">Passed</span>
                            : <span className="grade-failing">Failed</span>)
                        : ''}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          ) : (
            <div className="empty-state">
              <FaRegFileAlt className="empty-state-icon" />
              <div className="empty-state-text">No grades available for selected filters</div>
            </div>
          )}
        </div>
        <ChatWidget />
      </div>

      <div id="schedule" className="section-container">
        <div className="section-header">
          <h2><FaCalendarAlt style={{ marginRight: '10px' }} />Class Schedule</h2>
        </div>
        <div className="section-content">
          {loading.schedule ? (
            <div className="loading-container">
              <div className="loading-spinner" />
              <p>Loading schedule...</p>
            </div>
          ) : schedule.length ? (
            <table className="data-table">
              <thead>
                <tr>
                  <th>Ziua</th>
                  <th>Orele</th>
                  <th>Disciplina</th>
                  <th>Tipul</th>
                  <th>Sala</th>
                  <th>Profesor</th>
                  <th>Frecvența</th>
                </tr>
              </thead>
              <tbody>
                {schedule.map((item, index) => {
                  let itemClass = '';
                  if (item.tipul === 'Curs') itemClass = 'schedule-item-course';
                  else if (item.tipul === 'Laborator') itemClass = 'schedule-item-lab';
                  else if (item.tipul === 'Seminar') itemClass = 'schedule-item-seminar';

                  return (
                    <tr key={index} className={itemClass}>
                      <td>
                        <FaCalendarAlt style={{ marginRight: '8px', opacity: 0.7 }} /> {item.zi}
                      </td>
                      <td>
                        <FaClock style={{ marginRight: '8px', opacity: 0.7 }} /> {`${item.oraInceput}:00 - ${item.oraSfarsit}:00`}
                      </td>
                      <td>{item.disciplina}</td>
                      <td>{item.tipul}</td>
                      <td>
                        <FaMapMarkerAlt style={{ marginRight: '8px', opacity: 0.7 }} /> {item.sala}
                      </td>
                      <td>
                        <FaChalkboardTeacher style={{ marginRight: '8px', opacity: 0.7 }} /> {item.cadruDidactic}
                      </td>
                      <td>{item.frecventa === "saptamanal" ? "Weekly" : item.frecventa}</td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          ) : (
            <div className="empty-state">
              <FaCalendarAlt className="empty-state-icon" />
              <div className="empty-state-text">No schedule available</div>
            </div>
          )}
        </div>
      </div>

     {/* To-Do List Section */}
<div id="todo" className="section-container">
  <div className="section-header">
    <h2>To-Do List</h2>
  </div>

  <div className="section-content">
    {/* Formular pentru adăugare To-Do */}
    <div className="todo-form">
      <input
        type="text"
        placeholder="Titlu"
        value={newTitle}
        onChange={(e) => setNewTitle(e.target.value)}
      />
      <input
        type="text"
        placeholder="Descriere (opțional)"
        value={newDescription}
        onChange={(e) => setNewDescription(e.target.value)}
      />
      <input
        type="date"
        value={newDeadline}
        onChange={(e) => setNewDeadline(e.target.value)}
      />
      <button onClick={handleAddTodo}>Adaugă</button>
    </div>

    {/* Lista To-Do sau mesaj de încărcare */}
    {loadingTodos ? (
      <div className="loading-container">
        <div className="loading-spinner" />
        <p>Se încarcă To-Do-urile...</p>
      </div>
    ) : Array.isArray(todos) ? (
      todos.length > 0 ? (
        <table className="data-table">
          <thead>
            <tr>
              <th>Titlu</th>
              <th>Descriere</th>
              <th>Deadline</th>
              <th>Status</th>
              <th>Acțiuni</th>
            </tr>
          </thead>
          <tbody>
            {todos.map((todo, idx) => (
              <tr key={idx}>
                <td>{todo.title}</td>
                <td>{todo.description || "—"}</td>
                <td>{todo.deadline}</td>
                <td>{todo.done ? "Finalizat" : "Nefinalizat"}</td>
                <td>
                  {!todo.done && (
                    <button
                      onClick={() => handleMarkDone(todo.id)}
                      style={{ marginRight: "8px" }}
                    >
                      Marchează ca done
                    </button>
                  )}
                  <button onClick={() => handleDeleteTodo(todo.id)}>Șterge</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <p>Nu există niciun To-Do momentan.</p>
      )
    ) : (
      <p style={{ color: "red" }}>Eroare: lista de To-Do-uri nu este validă.</p>
    )}
  </div>
</div>


      <div style={{ textAlign: 'center', margin: '40px 0' }}>
        <button onClick={goToContractSelection} className="btn-generate">
          Generează Contractul de Studii pentru anul {student.an}
        </button>
      </div>
    </div>
  </div>
);
}
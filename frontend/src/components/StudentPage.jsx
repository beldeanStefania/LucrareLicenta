import React, { useEffect, useState } from "react";
import { request } from "../helpers/axios-helper";
import "./StudentPage.css";

export default function StudentPage({ onLogout }) {
  const [schedule, setSchedule] = useState([]);
  const [loading, setLoading] = useState(true);
  const [grades, setGrades] = useState([]);


  useEffect(() => {
    fetchSchedule();
    fetchGrades();
  }, []);

  const fetchSchedule = () => {
    request("GET", "/api/auth/userInfo") // Fetch user info to get group
      .then((response) => {
        const userGroup = response.data.grupa; // Group of the student
        return request("GET", `/api/orare/getAll/${userGroup}`); // Fetch schedule by group
      })
      .then((response) => {
        if (Array.isArray(response.data)) {
          setSchedule(response.data); // Store schedule if it's an array
        } else {
          setSchedule([]); // No data or incorrect format
          console.error("Invalid schedule data format:", response.data);
        }
      })
      .catch((error) => {
        console.error("Failed to fetch schedule:", error);
        setSchedule([]);
      })
      .finally(() => {
        setLoading(false);
      });
  };

  const fetchGrades = () => {
    request("GET", "/api/auth/userInfo") // Preluăm informațiile utilizatorului pentru a obține codul studentului
      .then((response) => {
        const studentCod = response.data.cod; // Codul studentului
        return request("GET", `/api/catalogStudentMaterie/getNote/${studentCod}`); // Preluăm notele pe baza codului
      })
      .then((response) => {
        if (Array.isArray(response.data)) {
          setGrades(response.data); // Stocăm notele în state dacă răspunsul este valid
        } else {
          setGrades([]); // Dacă nu există date, setăm o listă goală
          console.error("Invalid grades data format:", response.data);
        }
      })
      .catch((error) => {
        console.error("Failed to fetch grades:", error);
        setGrades([]);
      });
  };
  
  

  if (loading) {
    return <div>Loading...</div>;
  }

  return (
    <div className="student-page">
      {/* Header-ul paginii */}
      <div className="student-header">
        <h1>Student Dashboard</h1>
        <button className="logout-btn" onClick={onLogout}>
          Logout
        </button>
      </div>


      {/* Note */}
      <section>
  <h2>Your Grades</h2>
  {grades.length > 0 ? (
    <table className="grades-table">
      <thead>
        <tr>
          <th>Materie</th>
          <th>Cod</th>
          <th>Nota</th>
          <th>Semestru</th>
        </tr>
      </thead>
      <tbody>
        {grades.map((grade, index) => (
          <tr key={index}>
            <td>{grade.numeMaterie}</td>
            <td>{grade.codMaterie}</td>
            <td style={{ color: grade.nota < 5 ? 'red' : 'black' }}>
              {grade.nota}
            </td>
            <td>{grade.semestru}</td>
          </tr>
        ))}
      </tbody>
    </table>
  ) : (
    <p>No grades available for this student.</p>
  )}
</section>



      {/* Orar */}
      <section>
        <h2>Your Schedule</h2>
        {schedule.length > 0 ? (
          <table className="schedule-table">
          <thead>
            <tr>
              <th>Ziua</th>
              <th>Orele</th>
              <th>Frecvența</th>
              <th>Sala</th>
              <th>Tipul</th>
              <th>Formația</th>
              <th>Disciplina</th>
              <th>Cadru Didactic</th>
            </tr>
          </thead>
          <tbody>
  {schedule.map((item, index) => (
    <tr key={index}>
      <td>{item.zi}</td>
      <td>{`${item.oraInceput}:00 - ${item.oraSfarsit}:00`}</td>
      <td>{item.frecventa === "saptamanal" ? "" : item.frecventa}</td>
      <td>{item.sala}</td>
      <td>{item.tipul}</td>
      <td>{item.formatia}</td>
      <td>{item.disciplina}</td>
      <td>{item.cadruDidactic}</td>
    </tr>
  ))}
</tbody>

        </table>
        
        ) : (
          <p>No schedule available for your group.</p>
        )}
      </section>
    </div>
  );
}

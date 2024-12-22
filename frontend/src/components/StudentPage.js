import React, { useState, useEffect } from "react";
import { request } from "../helpers/axios-helper";

const StudentPage = () => {
  const [userInfo, setUserInfo] = useState(null);
  const [grades, setGrades] = useState([]);
  const [schedule, setSchedule] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Fetch user info
    request("GET", "/api/auth/userInfo")
      .then((response) => {
        setUserInfo(response.data || {}); // Fallback pentru date lipsă
      })
      .catch((error) => {
        console.error("Error fetching user info:", error);
        setUserInfo(null); // Setează userInfo la null pe eroare
      });

    // Fetch grades
    request("GET", "/api/catalogStudentMaterie/getNote/undefined")
      .then((response) => {
        setGrades(response.data || []); // Fallback pentru date lipsă
      })
      .catch((error) => {
        console.error("Error fetching grades:", error);
        setGrades([]); // Setează grades la array gol pe eroare
      });

    // Fetch schedule
    request("GET", "/api/orare/getAll/undefined")
      .then((response) => {
        setSchedule(response.data || []); // Fallback pentru date lipsă
      })
      .catch((error) => {
        console.error("Error fetching schedule:", error);
        setSchedule([]); // Setează schedule la array gol pe eroare
      })
      .finally(() => {
        setLoading(false); // Finalizează încărcarea
      });
  }, []);

  if (loading) {
    return <p>Loading...</p>; // Afișare mesaj de încărcare
  }

  return (
    <div style={{ padding: "20px" }}>
      <h1>Welcome,</h1>
      <h2>Your Information</h2>
      <p>Username: {userInfo?.username || "Not available"}</p>
      <p>Cod: {userInfo?.cod || "Not available"}</p>
      <p>Grupa: {userInfo?.grupa || "Not available"}</p>

      <h2>Your Grades</h2>
      {grades.length > 0 ? (
        <ul>
          {grades.map((grade, index) => (
            <li key={index}>
              Materie: {grade.codMaterie}, Nota: {grade.nota}, Semestru: {grade.semestru}
            </li>
          ))}
        </ul>
      ) : (
        <p>No grades available for this student.</p>
      )}

      <h2>Your Schedule</h2>
      {schedule.length > 0 ? (
        <ul>
          {schedule.map((item, index) => (
            <li key={index}>
              Zi: {item.zi}, Ora început: {item.oraInceput}, Ora sfârșit: {item.oraSfarsit}
            </li>
          ))}
        </ul>
      ) : (
        <p>No schedule available for your group.</p>
      )}
    </div>
  );
};

export default StudentPage;

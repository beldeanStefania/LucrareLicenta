import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { request } from "../helpers/axios-helper";

export default function StudentPage() {
  const { studentCod } = useParams(); // Codul studentului din URL
  const [grades, setGrades] = useState([]);
  const [newGrade, setNewGrade] = useState({
    nota: "",
    semestru: "",
    codMaterie: "",
  });
  const [loading, setLoading] = useState(true);

  // Fetch grades for the student
  useEffect(() => {
    request("GET", `/api/catalogStudentMaterie/getNote/${studentCod}`)
      .then((response) => {
        setGrades(response.data);
        setLoading(false);
      })
      .catch((error) => {
        console.error("Error fetching grades:", error);
        setLoading(false);
      });
  }, [studentCod]);

  // Add a new grade
  const addGrade = () => {
    const gradeToAdd = { ...newGrade, studentCod };
    request("POST", "/api/catalogStudentMaterie/add", gradeToAdd)
      .then(() => {
        alert("Grade added successfully!");
        setNewGrade({ nota: "", semestru: "", codMaterie: "" });
        refreshGrades();
      })
      .catch((error) => console.error("Error adding grade:", error));
  };

  // Delete a grade
  const deleteGrade = (materieCod) => {
    request("DELETE", `/api/catalogStudentMaterie/delete/${studentCod}/${materieCod}`)
      .then(() => {
        alert("Grade deleted successfully!");
        refreshGrades();
      })
      .catch((error) => console.error("Error deleting grade:", error));
  };

  // Refresh grades
  const refreshGrades = () => {
    request("GET", `/api/catalogStudentMaterie/getNote/${studentCod}`)
      .then((response) => setGrades(response.data))
      .catch((error) => console.error("Error refreshing grades:", error));
  };

  if (loading) {
    return <div>Loading...</div>;
  }

  return (
    <div style={{ padding: "20px" }}>
      <h1>Grades for Student {studentCod}</h1>

      <section>
        <h2>Grades</h2>
        {grades.length > 0 ? (
          <ul>
            {grades.map((grade, index) => (
              <li key={index}>
                Materie: {grade.codMaterie}, Nota: {grade.nota}, Semestru: {grade.semestru}
                <button
                  style={{ marginLeft: "10px", padding: "5px 10px", cursor: "pointer" }}
                  onClick={() => deleteGrade(grade.codMaterie)}
                >
                  Delete
                </button>
              </li>
            ))}
          </ul>
        ) : (
          <div>No grades available for this student.</div>
        )}
      </section>

      <section>
        <h2>Add Grade</h2>
        <form
          onSubmit={(e) => {
            e.preventDefault();
            addGrade();
          }}
        >
          <input
            type="text"
            placeholder="Cod Materie"
            value={newGrade.codMaterie}
            onChange={(e) => setNewGrade({ ...newGrade, codMaterie: e.target.value })}
          />
          <input
            type="number"
            placeholder="Nota"
            value={newGrade.nota}
            onChange={(e) => setNewGrade({ ...newGrade, nota: parseFloat(e.target.value) })}
          />
          <input
            type="number"
            placeholder="Semestru"
            value={newGrade.semestru}
            onChange={(e) => setNewGrade({ ...newGrade, semestru: parseInt(e.target.value) })}
          />
          <button type="submit">Add</button>
        </form>
      </section>
    </div>
  );
}

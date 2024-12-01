import React, { useState, useEffect } from "react";
import { request } from "../helpers/axios-helper";

export default function AdminPage({ onLogout }) {
  const [activeSection, setActiveSection] = useState(""); // Control pentru secțiunea activă
  const [students, setStudents] = useState([]);
  const [newStudent, setNewStudent] = useState({
    cod: "",
    nume: "",
    prenume: "",
    grupa: "",
    an: "",
    username: "",
    password: "",
  });
  const [editStudent, setEditStudent] = useState({
    id: "",
    nume: "",
    prenume: "",
    grupa: "",
    an: "",
    username: "",
    password: "",
  });

  // Fetch all students
  const fetchStudents = () => {
    request("GET", "/api/student/getAll", {})
      .then((response) => setStudents(response.data))
      .catch((error) => console.error("Error fetching students:", error));
  };

  // Add new student
  const addStudent = () => {
    request("POST", "/api/student/add", newStudent)
      .then(() => {
        alert("Student added successfully!");
        setNewStudent({
          cod: "",
          nume: "",
          prenume: "",
          grupa: "",
          an: "",
          username: "",
          password: "",
        });
        fetchStudents();
        setActiveSection(""); // Resetare secțiune
      })
      .catch((error) => console.error("Error adding student:", error));
  };

  // Update student
  const updateStudent = () => {
    if (!editStudent.cod || editStudent.cod.trim() === "") {
      alert("Please provide a valid Cod.");
      return;
    }
  
    request("PUT", `/api/student/update/${editStudent.cod}`, editStudent)
      .then(() => {
        alert("Student updated successfully!");
        setEditStudent({
          cod: "",
          nume: "",
          prenume: "",
          grupa: "",
          an: "",
          username: "",
          password: "",
        });
        fetchStudents(); // Actualizează lista de studenți
        setActiveSection(""); // Resetează secțiunea activă
      })
      .catch((error) => {
        console.error("Error updating student:", error);
        alert("Failed to update student. Please check the Cod and try again.");
      });
  };
  
  

  // Delete student
  const deleteStudent = (firstName, lastName) => {
    request("DELETE", `/api/student/delete/${firstName}/${lastName}`, {})
      .then(() => {
        alert("Student deleted successfully!");
        fetchStudents();
        setActiveSection(""); // Resetare secțiune
      })
      .catch((error) => console.error("Error deleting student:", error));
  };

  useEffect(() => {
    fetchStudents();
  }, []);

  return (
    <div style={{ padding: "20px" }}>
      <button
        style={{
          position: "absolute",
          top: "10px",
          right: "10px",
          padding: "10px 20px",
          backgroundColor: "#dc3545",
          color: "#fff",
          border: "none",
          borderRadius: "5px",
          cursor: "pointer",
        }}
        onClick={onLogout}
      >
        Logout
      </button>

      <h1>Admin Dashboard</h1>

      {/* Butoanele pentru funcționalități */}
      <div style={{ marginBottom: "20px" }}>
  <button onClick={() => setActiveSection("view")} style={buttonStyle}>
    View All Students
  </button>
  <button onClick={() => setActiveSection("add")} style={buttonStyle}>
    Add Student
  </button>
  <button onClick={() => setActiveSection("edit")} style={buttonStyle}>
    Edit Student
  </button>
  <button onClick={() => setActiveSection("delete")} style={buttonStyle}>
    Delete Student
  </button>
</div>


      {/* Afișarea secțiunilor bazate pe selecție */}
      {activeSection === "view" && (
  <section>
    <h2>All Students</h2>
    <ul>
      {students.map((student) => (
        <li key={student.cod}>
          Cod: {student.cod} - {student.nume} {student.prenume} - Grupa: {student.grupa}, An: {student.an}
        </li>
      ))}
    </ul>
  </section>
)}


      {activeSection === "add" && (
        <section>
          <h2>Add Student</h2>
          <form onSubmit={(e) => { e.preventDefault(); addStudent(); }}>
            <input
              type="text"
              placeholder="Cod"
              value={newStudent.cod}
              onChange={(e) => setNewStudent({ ...newStudent, cod: e.target.value })}
            />
            <input
              type="text"
              placeholder="Nume"
              value={newStudent.nume}
              onChange={(e) => setNewStudent({ ...newStudent, nume: e.target.value })}
            />
            <input
              type="text"
              placeholder="Prenume"
              value={newStudent.prenume}
              onChange={(e) => setNewStudent({ ...newStudent, prenume: e.target.value })}
            />
            <input
              type="text"
              placeholder="Grupa"
              value={newStudent.grupa}
              onChange={(e) => setNewStudent({ ...newStudent, grupa: e.target.value })}
            />
            <input
              type="number"
              placeholder="An"
              value={newStudent.an}
              onChange={(e) => setNewStudent({ ...newStudent, an: parseInt(e.target.value) })}
            />
            <input
              type="text"
              placeholder="Username"
              value={newStudent.username}
              onChange={(e) => setNewStudent({ ...newStudent, username: e.target.value })}
            />
            <input
              type="password"
              placeholder="Password"
              value={newStudent.password}
              onChange={(e) => setNewStudent({ ...newStudent, password: e.target.value })}
            />
            <button type="submit">Add</button>
          </form>
        </section>
      )}

{activeSection === "edit" && (
  <section>
    <h2>Edit Student</h2>
    <form onSubmit={(e) => { e.preventDefault(); updateStudent(); }}>
      <input
        type="text"
        placeholder="Cod"
        value={editStudent.cod}
        onChange={(e) => setEditStudent({ ...editStudent, cod: e.target.value })}
      />
      <input
        type="text"
        placeholder="Nume"
        value={editStudent.nume}
        onChange={(e) => setEditStudent({ ...editStudent, nume: e.target.value })}
      />
      <input
        type="text"
        placeholder="Prenume"
        value={editStudent.prenume}
        onChange={(e) => setEditStudent({ ...editStudent, prenume: e.target.value })}
      />
      <input
        type="text"
        placeholder="Grupa"
        value={editStudent.grupa}
        onChange={(e) => setEditStudent({ ...editStudent, grupa: e.target.value })}
      />
      <input
        type="number"
        placeholder="An"
        value={editStudent.an}
        onChange={(e) => setEditStudent({ ...editStudent, an: parseInt(e.target.value) })}
      />
      <button type="submit">Update</button>
    </form>
  </section>
)}



      {activeSection === "delete" && (
        <section>
          <h2>Delete Student</h2>
          <ul>
            {students.map((student) => (
              <li key={student.id}>
                {student.nume} {student.prenume}{" "}
                <button onClick={() => deleteStudent(student.nume, student.prenume)}>Delete</button>
              </li>
            ))}
          </ul>
        </section>
      )}
    </div>
  );
}

// Stiluri pentru butoane
const buttonStyle = {
  margin: "5px",
  padding: "10px 20px",
  backgroundColor: "#007bff",
  color: "#fff",
  border: "none",
  borderRadius: "5px",
  cursor: "pointer",
};

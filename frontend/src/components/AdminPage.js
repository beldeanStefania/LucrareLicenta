import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { request, setAuthHeader } from "../helpers/axios-helper";
import "./AdminPage.css";

export default function AdminPage({ onLogout }) {
  const [students, setStudents] = useState([]);
  const [viewMode, setViewMode] = useState("list"); // Modes: "list", "add", "update"
  const [studentToEdit, setStudentToEdit] = useState(null);
  const navigate = useNavigate();

  // Fetch all students
  useEffect(() => {
    fetchStudents();
  }, []);

  const fetchStudents = () => {
    request("GET", "/api/student/getAllStudents")
      .then((response) => {
        console.log("Students data:", response.data);
        setStudents(response.data); // Set the full student list
      })
      .catch((error) => {
        console.error("Failed to fetch students", error);
      });
  };

  const handleAddStudent = () => {
    setViewMode("add");
    setStudentToEdit(null); // Resetează studentToEdit pentru formularul de adăugare
  };

  const handleEditStudent = (student) => {
    if (!student || !student.cod) {
      alert("Cannot edit student. Missing student cod.");
      return;
    }

    setStudentToEdit(student);
    setViewMode("update");
  };

  const handleDeleteStudent = (cod) => {
    if (window.confirm("Are you sure you want to delete this student?")) {
      request("DELETE", `/api/student/delete/${cod}`)
        .then(() => {
          alert("Student deleted successfully!");
          fetchStudents();
        })
        .catch((error) => {
          console.error("Failed to delete student:", error);
          alert("Failed to delete student. Please try again.");
        });
    }
  };

  const handleSaveStudent = (student) => {
    const endpoint = viewMode === "add" ? "/api/student/add" : `/api/student/update/${student.cod}`;
    const method = viewMode === "add" ? "POST" : "PUT";

    if (viewMode === "update") {
      // Exclude `password`, `username`, și `cod` din obiectul pentru actualizare
      delete student.password;
      delete student.username;
      delete student.cod;
    }

    request(method, endpoint, student)
      .then(() => {
        alert(viewMode === "add" ? "Student added successfully!" : "Student updated successfully!");
        setViewMode("list");
        fetchStudents();
      })
      .catch((error) => {
        console.error(viewMode === "add" ? "Failed to add student:" : "Failed to update student:", error);
        alert(viewMode === "add" ? "Failed to add student. Please try again." : "Failed to update student. Please try again.");
      });
  };

  const renderStudentForm = () => (
    <div className="form-container">
      <h2>{viewMode === "add" ? "Add Student" : "Edit Student"}</h2>
      <form
        onSubmit={(e) => {
          e.preventDefault();
          const formData = new FormData(e.target);
          const student = Object.fromEntries(formData.entries());
          handleSaveStudent(student);
        }}
      >
        <div className="form-group">
          <label>First Name:</label>
          <input
            name="nume"
            defaultValue={studentToEdit?.nume || ""}
            required
            className="input-field"
          />
        </div>
        <div className="form-group">
          <label>Last Name:</label>
          <input
            name="prenume"
            defaultValue={studentToEdit?.prenume || ""}
            required
            className="input-field"
          />
        </div>
        {viewMode === "add" && (
          <>
            <div className="form-group">
              <label>Code:</label>
              <input
                name="cod"
                defaultValue={studentToEdit?.cod || ""}
                required
                className="input-field"
              />
            </div>
            <div className="form-group">
              <label>Username:</label>
              <input
                name="username"
                defaultValue={studentToEdit?.username || ""}
                required
                className="input-field"
              />
            </div>
            <div className="form-group">
              <label>Password:</label>
              <input
                type="password"
                name="password"
                required
                className="input-field"
              />
            </div>
          </>
        )}
        <div className="form-group">
          <label>Year:</label>
          <input
            type="number"
            name="an"
            defaultValue={studentToEdit?.an || ""}
            required
            className="input-field"
          />
        </div>
        <div className="form-group">
          <label>Group:</label>
          <input
            name="grupa"
            defaultValue={studentToEdit?.grupa || ""}
            required
            className="input-field"
          />
        </div>
        <div className="form-buttons">
          <button type="submit" className="btn save-btn">
            {viewMode === "add" ? "Add Student" : "Save"}
          </button>
          <button type="button" className="btn cancel-btn" onClick={() => setViewMode("list")}>
            Cancel
          </button>
        </div>
      </form>
    </div>
  );

  const renderStudentList = () => (
    <div className="list-container">
      <h1>Students List</h1>
      <button className="btn add-btn" onClick={handleAddStudent}>Add Student</button>
      {students.length > 0 ? (
        <table className="students-table">
          <thead>
            <tr>
              <th>First Name</th>
              <th>Last Name</th>
              <th>Year</th>
              <th>Code</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {students.map((student) => (
              <tr key={student.cod}>
                <td>{student.nume}</td>
                <td>{student.prenume}</td>
                <td>{student.an}</td>
                <td>{student.cod || "N/A"}</td>
                <td>
                  <button
                    className="btn edit-btn"
                    onClick={() => handleEditStudent(student)}
                  >
                    Edit
                  </button>
                  <button
                    className="btn delete-btn"
                    onClick={() => handleDeleteStudent(student.cod)}
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <p>No students available</p>
      )}
    </div>
  );

  return (
    <div className="admin-page">
      <div className="header">
        <h1>Admin Dashboard</h1>
        <button className="btn btn-primary logout-btn" onClick={onLogout}>
          Logout
        </button>
      </div>
      {viewMode === "list" && renderStudentList()}
      {(viewMode === "add" || viewMode === "update") && renderStudentForm()}
    </div>
  );
}

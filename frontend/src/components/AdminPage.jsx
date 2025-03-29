// AdminPage.jsx
import React, { useEffect, useState } from "react";
import { request } from "../helpers/axios-helper";
import NavigationHeader from "./NavigationHeader";
import "./AdminPage.css";

export default function AdminPage({ onLogout }) {
  // ---------------------------
  // STATE
  // ---------------------------
  const [students, setStudents] = useState([]);
  const [viewMode, setViewMode] = useState("list");
  const [studentToEdit, setStudentToEdit] = useState(null);

  const [professors, setProfessors] = useState([]);
  const [viewModeProf, setViewModeProf] = useState("list");
  const [professorToEdit, setProfessorToEdit] = useState(null);

  const [subjects, setSubjects] = useState([]);
  const [subjectMode, setSubjectMode] = useState("list");
  const [subjectToEdit] = useState(null);

  const [subjectFormVisible, setSubjectFormVisible] = useState(false);
  const [selectedProfForSubject, setSelectedProfForSubject] = useState(null);
  const [allSubjects, setAllSubjects] = useState([]);
  const [useCustomSubject, setUseCustomSubject] = useState(false);
  const [customSubject, setCustomSubject] = useState("");
  const [selectedSubject, setSelectedSubject] = useState("");

  useEffect(() => {
    fetchStudents();
    fetchProfessors();
    fetchSubjects();
    fetchAllSubjects();
  }, []);

  // ---------------------------
  // STUDENTS
  // ---------------------------
  const fetchStudents = () => {
    request("GET", "/api/student/getAllStudents")
      .then((res) => setStudents(res.data))
      .catch((err) => console.error("Failed to fetch students", err));
  };

  const handleAddStudent = () => {
    setViewMode("add");
    setStudentToEdit(null);
  };

  const handleEditStudent = (student) => {
    setViewMode("update");
    setStudentToEdit(student);
  };

  const handleDeleteStudent = (cod) => {
    if (window.confirm("Are you sure you want to delete this student?")) {
      request("DELETE", `/api/student/delete/${cod}`)
        .then(() => {
          alert("Student deleted successfully!");
          fetchStudents();
        })
        .catch((err) => {
          console.error("Failed to delete student:", err);
          alert("Failed to delete student. Please try again.");
        });
    }
  };

  const handleSaveStudent = (student) => {
    const endpoint =
      viewMode === "add"
        ? "/api/student/add"
        : `/api/student/update/${student.cod}`;
    const method = viewMode === "add" ? "POST" : "PUT";

    if (viewMode === "update") {
      delete student.password;
      delete student.username;
      delete student.cod;
    }

    request(method, endpoint, student)
      .then(() => {
        alert(
          viewMode === "add"
            ? "Student added successfully!"
            : "Student updated successfully!"
        );
        setViewMode("list");
        fetchStudents();
      })
      .catch((error) => {
        console.error("Error:", error);
        alert("Failed to save student.");
      });
  };

  const renderStudentList = () => (
    <div className="list-container">
      <h2>Students List</h2>
      <button className="btn add-btn" onClick={handleAddStudent}>Add Student</button>
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
                <button className="btn edit-btn" onClick={() => handleEditStudent(student)}>Edit</button>
                <button className="btn delete-btn" onClick={() => handleDeleteStudent(student.cod)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );

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
        <input name="nume" placeholder="First Name" defaultValue={studentToEdit?.nume || ""} required />
        <input name="prenume" placeholder="Last Name" defaultValue={studentToEdit?.prenume || ""} required />
        {viewMode === "add" && (
          <>
            <input name="cod" placeholder="Code" required />
            <input name="username" placeholder="Username" required />
            <input name="password" type="password" placeholder="Password" required />
          </>
        )}
        <input name="an" type="number" placeholder="Year" defaultValue={studentToEdit?.an || ""} required />
        <input name="grupa" placeholder="Group" defaultValue={studentToEdit?.grupa || ""} required />
        <button type="submit" className="btn save-btn">{viewMode === "add" ? "Add" : "Save"}</button>
        <button type="button" className="btn cancel-btn" onClick={() => setViewMode("list")}>Cancel</button>
      </form>
    </div>
  );

  // ---------------------------
  // PROFESSORS
  // ---------------------------
  const handleAddProfessor = () => {
    setViewModeProf("add");
    setProfessorToEdit(null);
  };

  const handleEditProfessor = (professor) => {
    setViewModeProf("update");
    setProfessorToEdit(professor);
  };

  const handleDeleteProfessor = (prof) => {
    if (window.confirm(`Delete professor ${prof.nume} ${prof.prenume}?`)) {
      request("DELETE", `/api/profesor/delete/${prof.nume}/${prof.prenume}`)
        .then(() => {
          alert("Deleted successfully!");
          fetchProfessors();
        })
        .catch((err) => alert("Failed to delete professor."));
    }
  };

  const handleSaveProfessor = (profData) => {
    const endpoint =
      viewModeProf === "add"
        ? "/api/profesor/add"
        : `/api/profesor/update/${professorToEdit.id}`;
    const method = viewModeProf === "add" ? "POST" : "PUT";

    request(method, endpoint, profData)
      .then(() => {
        alert("Saved!");
        setViewModeProf("list");
        fetchProfessors();
      })
      .catch((err) => alert("Failed to save professor."));
  };

  const renderProfessorList = () => (
    <div className="list-container">
      <h2>Professors List</h2>
      <button className="btn add-btn" onClick={handleAddProfessor}>Add Professor</button>
      <table className="students-table">
        <thead>
          <tr>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Username</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {professors.map((prof) => (
            <tr key={prof.id}>
              <td>{prof.nume}</td>
              <td>{prof.prenume}</td>
              <td>{prof.user?.username || "N/A"}</td>
              <td>
                <button className="btn edit-btn" onClick={() => handleEditProfessor(prof)}>Edit</button>
                <button className="btn delete-btn" onClick={() => handleDeleteProfessor(prof)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );

  const renderProfessorForm = () => (
    <div className="form-container">
      <h2>{viewModeProf === "add" ? "Add Professor" : "Edit Professor"}</h2>
      <form
        onSubmit={(e) => {
          e.preventDefault();
          const formData = new FormData(e.target);
          const data = Object.fromEntries(formData.entries());
          handleSaveProfessor(data);
        }}
      >
        <input name="nume" placeholder="First Name" defaultValue={professorToEdit?.nume || ""} required />
        <input name="prenume" placeholder="Last Name" defaultValue={professorToEdit?.prenume || ""} required />
        <input name="username" placeholder="Username" defaultValue={professorToEdit?.user?.username || ""} required={viewModeProf === "add"} />
        <input name="password" type="password" placeholder="Password" required={viewModeProf === "add"} />
        <button type="submit" className="btn save-btn">{viewModeProf === "add" ? "Add" : "Save"}</button>
        <button type="button" className="btn cancel-btn" onClick={() => setViewModeProf("list")}>Cancel</button>
      </form>
    </div>
  );

  // ---------------------------
  // SUBJECTS (Materii)
  // ---------------------------
  const handleAddSubject = () => {
    setSubjectMode("add");
  };

  const handleSaveSubject = (subject) => {
    request("POST", "/api/materie/add", subject)
      .then(() => {
        alert("Subject added successfully!");
        fetchSubjects();
        setSubjectMode("list");
      })
      .catch(() => alert("Failed to add subject."));
  };

  const renderSubjectList = () => (
    <div className="list-container">
      <h2>Subjects List</h2>
      <button className="btn add-btn" onClick={handleAddSubject}>Add Subject</button>
      <table className="students-table">
        <thead>
          <tr>
            <th>Name</th>
            <th>Semester</th>
            <th>Code</th>
            <th>Credits</th>
          </tr>
        </thead>
        <tbody>
          {subjects.map((subject) => (
            <tr key={subject.id}>
              <td>{subject.nume}</td>
              <td>{subject.semestru}</td>
              <td>{subject.cod}</td>
              <td>{subject.credite}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );

  const renderSubjectAddForm = () => (
    <div className="form-container">
      <h2>Add Subject</h2>
      <form
        onSubmit={(e) => {
          e.preventDefault();
          const formData = new FormData(e.target);
          const data = Object.fromEntries(formData.entries());
          data.credite = parseInt(data.credite);
          data.semestru = parseInt(data.semestru);
          handleSaveSubject(data);
        }}
      >
        <input name="nume" placeholder="Name" required className="input-field" />
        <input name="cod" placeholder="Code" required className="input-field" />
        <input name="semestru" placeholder="Semester" type="number" required className="input-field" />
        <input name="credite" placeholder="Credits" type="number" required className="input-field" />
        <button type="submit" className="btn save-btn">Add</button>
        <button type="button" className="btn cancel-btn" onClick={() => setSubjectMode("list")}>Cancel</button>
      </form>
    </div>
  );

  return (
    <div className="admin-page">
      <NavigationHeader userRole="ROLE_ADMIN" userName="Admin" onLogout={onLogout} />
      <div className="admin-content">
        <hr />
        <h1>Students Management</h1>
        {viewMode === "list" && renderStudentList()}
        {(viewMode === "add" || viewMode === "update") && renderStudentForm()}

        <hr />
        <h1>Professors Management</h1>
        {viewModeProf === "list" && renderProfessorList()}
        {(viewModeProf === "add" || viewModeProf === "update") && renderProfessorForm()}

        <hr />
        <h1>Subjects Management</h1>
        {subjectMode === "list" && renderSubjectList()}
        {subjectMode === "add" && renderSubjectAddForm()}
      </div>
    </div>
  );
}

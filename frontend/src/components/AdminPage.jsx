import React, { useEffect, useState } from "react";
import { request } from "../helpers/axios-helper";
import NavigationHeader from "./NavigationHeader"; 
import "./AdminPage.css";

export default function AdminPage({ onLogout }) {
  // ---------------------------
  // STUDENT LOGIC
  // ---------------------------
  const [students, setStudents] = useState([]);
  const [viewMode, setViewMode] = useState("list"); // "list", "add", "update"
  const [studentToEdit, setStudentToEdit] = useState(null);

  // ---------------------------
  // PROFESSOR LOGIC
  // ---------------------------
  const [professors, setProfessors] = useState([]);
  const [viewModeProf, setViewModeProf] = useState("list"); // "list", "add", "update"
  const [professorToEdit, setProfessorToEdit] = useState(null);

  // ---------------------------
  // SUBJECT/REPARTIZARE LOGIC
  // ---------------------------
  const [subjectFormVisible, setSubjectFormVisible] = useState(false);
  const [selectedProfForSubject, setSelectedProfForSubject] = useState(null);
  const [allSubjects, setAllSubjects] = useState([]); // Materii din DB

  // ---------------------------
  // On mount, fetch both lists
  // ---------------------------
  useEffect(() => {
    fetchStudents();
    fetchProfessors();
    fetchAllSubjects(); // Dacă ai un endpoint pentru a obține lista tuturor materiilor
  }, []);

  // ========== STUDENTS ==========
  const fetchStudents = () => {
    request("GET", "/api/student/getAllStudents")
      .then((response) => {
        setStudents(response.data);
      })
      .catch((error) => {
        console.error("Failed to fetch students", error);
      });
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
        .catch((error) => {
          console.error("Failed to delete student:", error);
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
            alert(viewMode === "add" ? "Student added successfully!" : "Student updated successfully!");
            setViewMode("list");
            fetchStudents();
        })
        .catch((error) => {
            console.error("Error:", error);

            if (error.response) {
                console.log("Răspuns Backend:", error.response);
                
                let errorMessage = "Eroare necunoscută.";
                
                if (error.response.data) {
                    if (typeof error.response.data === "string") {
                        errorMessage = error.response.data;
                    } else if (error.response.data.error) {
                        errorMessage = error.response.data.error;
                    } else if (error.response.data.message) {
                        errorMessage = error.response.data.message;
                    }
                } else if (error.response.status === 400) {
                    errorMessage = "Date invalide! Verifică dacă ai completat corect toate câmpurile.";
                } else if (error.response.status === 409) {
                    errorMessage = "Codul studentului trebuie să fie unic! Un student cu acest cod există deja.";
                }

                alert(`Eroare: ${errorMessage}`);
            } else {
                alert("Eroare: Nu s-a putut contacta serverul.");
            }
        });
};


  
  
const renderStudentList = () => (
  <div className="list-container">
    <h2>Students List</h2>
    <button className="btn add-btn" onClick={handleAddStudent}>
      Add Student
    </button>
    {students.length > 0 ? (
      <div className="scroll-container">
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
    ) : (
      <p>No students available</p>
    )}
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
                defaultValue=""
                required
                className="input-field"
              />
            </div>
            <div className="form-group">
              <label>Username:</label>
              <input name="username" required className="input-field" />
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
          <button
            type="button"
            className="btn cancel-btn"
            onClick={() => setViewMode("list")}
          >
            Cancel
          </button>
        </div>
      </form>
    </div>
  );

  // ========== PROFESSORS ==========

  const fetchProfessors = () => {
    request("GET", "/api/profesor/getAll")
      .then((response) => {
        setProfessors(response.data);
      })
      .catch((error) => {
        console.error("Failed to fetch professors", error);
      });
  };

  const handleAddProfessor = () => {
    setViewModeProf("add");
    setProfessorToEdit(null);
  };

  const handleEditProfessor = (professor) => {
    setViewModeProf("update");
    setProfessorToEdit(professor);
  };

  const handleDeleteProfessor = (prof) => {
    if (
      window.confirm(
        `Are you sure you want to delete professor ${prof.nume} ${prof.prenume}?`
      )
    ) {
      // Endpoint: DELETE /api/profesor/delete/{nume}/{prenume}
      request("DELETE", `/api/profesor/delete/${prof.nume}/${prof.prenume}`)
        .then(() => {
          alert("Professor deleted successfully!");
          fetchProfessors();
        })
        .catch((error) => {
          console.error("Failed to delete professor:", error);
          alert("Failed to delete professor. Please try again.");
        });
    }
  };

  const handleSaveProfessor = (profData) => {
    // la add => POST /api/profesor/add
    // la update => PUT /api/profesor/update/{id}
    if (viewModeProf === "add") {
      request("POST", "/api/profesor/add", profData)
        .then(() => {
          alert("Professor added successfully!");
          setViewModeProf("list");
          fetchProfessors();
        })
        .catch((error) => {
          console.error("Failed to add professor:", error);
          alert("Failed to add professor. Please try again.");
        });
    } else {
      const endpoint = `/api/profesor/update/${professorToEdit.id}`;
      request("PUT", endpoint, profData)
        .then(() => {
          alert("Professor updated successfully!");
          setViewModeProf("list");
          fetchProfessors();
        })
        .catch((error) => {
          console.error("Failed to update professor:", error);
          alert("Failed to update professor. Please try again.");
        });
    }
  };

  const renderProfessorList = () => (
    <div className="list-container">
      <h2>Professors List</h2>
      <button className="btn add-btn" onClick={handleAddProfessor}>
        Add Professor
      </button>
      {professors.length > 0 ? (
        <div className="scroll-container">
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
                    <button className="btn add-btn" onClick={() => handleOpenSubjectForm(prof)}>Add Subject</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ) : (
        <p>No professors available</p>
      )}
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
          // data = { nume, prenume, username, password }
          // la update, password poate fi gol => atunci nu se schimbă
          handleSaveProfessor(data);
        }}
      >
        <div className="form-group">
          <label>First Name (nume):</label>
          <input
            name="nume"
            defaultValue={professorToEdit?.nume || ""}
            required
            className="input-field"
          />
        </div>
        <div className="form-group">
          <label>Last Name (prenume):</label>
          <input
            name="prenume"
            defaultValue={professorToEdit?.prenume || ""}
            required
            className="input-field"
          />
        </div>
        <div className="form-group">
          <label>Username:</label>
          <input
            name="username"
            defaultValue={professorToEdit?.user?.username || ""}
            required={viewModeProf === "add"}
            className="input-field"
          />
        </div>
        <div className="form-group">
          <label>Password:</label>
          <input
            name="password"
            type="password"
            placeholder={
              viewModeProf === "update" ? "Leave empty if unchanged" : ""
            }
            required={viewModeProf === "add"} // la add e obligatoriu
            className="input-field"
          />
        </div>

        <div className="form-buttons">
          <button type="submit" className="btn save-btn">
            {viewModeProf === "add" ? "Add Professor" : "Save"}
          </button>
          <button
            type="button"
            className="btn cancel-btn"
            onClick={() => setViewModeProf("list")}
          >
            Cancel
          </button>
        </div>
      </form>
    </div>
  );

  // ========== SUBJECTS / REPARTIZARE PROF ==========
  const [useCustomSubject, setUseCustomSubject] = useState(false);
  const [customSubject, setCustomSubject] = useState("");
  const [selectedSubject, setSelectedSubject] = useState("");

  // Ex: /api/materie/getAll => toate materiile din DB
  const fetchAllSubjects = () => {
    request("GET", "/api/materie/getAll")
      .then((response) => {
        setAllSubjects(response.data);
      })
      .catch((error) => console.error("Failed to fetch subjects:", error));
  };

  const handleOpenSubjectForm = (prof) => {
    setSelectedProfForSubject(prof);
    setSubjectFormVisible(true);
    setUseCustomSubject(false);
    setCustomSubject("");
    setSelectedSubject("");
  };

  const createNewSubject = (subjectName) => {
    return request("POST", "/api/materie/add", { numeMaterie: subjectName })
      .then(response => {
        // Refresh subject list after adding
        fetchAllSubjects();
        return response.data;
      });
  };

  const handleSubmitSubject = (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    const data = Object.fromEntries(formData.entries());
    // data = { materie, tip } or { customMaterie, tip }
    
    const subjectName = useCustomSubject ? customSubject : data.materie;
    
    // If using custom subject, create it first
    const assignSubject = () => {
      // RepartizareProfDTO: { tip, materie, numeProfesor, prenumeProfesor }
      const payload = {
        tip: data.tip,
        materie: subjectName,
        numeProfesor: selectedProfForSubject.nume,
        prenumeProfesor: selectedProfForSubject.prenume,
      };
  
      request("POST", "/api/repartizareProf", payload)
        .then(() => {
          alert("Subject assigned successfully!");
          setSubjectFormVisible(false);
          setUseCustomSubject(false);
          setCustomSubject("");
        })
        .catch((error) => {
          console.error("Failed to assign subject:", error);
          alert("Failed to assign subject. Please try again.");
        });
    };
    
    if (useCustomSubject && customSubject.trim()) {
      // First create the new subject, then assign it
      createNewSubject(customSubject.trim())
        .then(() => {
          assignSubject();
        })
        .catch(error => {
          console.error("Failed to create new subject:", error);
          alert("Failed to create new subject. Please try again.");
        });
    } else {
      // Just assign the selected subject
      assignSubject();
    }
  };

  const renderSubjectForm = () => {
    if (!subjectFormVisible || !selectedProfForSubject) return null;

    return (
      <div className="form-container">
        <h2>
          Add Subject for {selectedProfForSubject.nume}{" "}
          {selectedProfForSubject.prenume}
        </h2>
        <form onSubmit={handleSubmitSubject}>
          <div className="form-group">
            <div className="subject-selection-mode">
              <label style={{ marginRight: 'var(--spacing-md)' }}>
                <input 
                  type="radio" 
                  name="subjectMode" 
                  checked={!useCustomSubject}
                  onChange={() => setUseCustomSubject(false)}
                  style={{ marginRight: 'var(--spacing-xs)' }} 
                />
                Select existing subject
              </label>
              <label>
                <input 
                  type="radio" 
                  name="subjectMode" 
                  checked={useCustomSubject}
                  onChange={() => setUseCustomSubject(true)}
                  style={{ marginRight: 'var(--spacing-xs)' }} 
                />
                Add new subject
              </label>
            </div>
          </div>

          {!useCustomSubject ? (
            <div className="form-group">
              <label>Materie:</label>
              <select 
                name="materie" 
                className="input-field" 
                required={!useCustomSubject}
                value={selectedSubject}
                onChange={(e) => setSelectedSubject(e.target.value)}
              >
                <option value="">Select a subject</option>
                {allSubjects.map((sub) => (
                  <option key={sub.id} value={sub.nume}>
                    {sub.nume}
                  </option>
                ))}
              </select>
            </div>
          ) : (
            <div className="form-group">
              <label>New Subject Name:</label>
              <input
                name="customMaterie"
                className="input-field"
                required={useCustomSubject}
                value={customSubject}
                onChange={(e) => setCustomSubject(e.target.value)}
                placeholder="Enter a new subject name"
              />
            </div>
          )}

          <div className="form-group">
            <label>Tip:</label>
            <select name="tip" className="input-field" required>
              <option value="">Select type</option>
              <option value="Curs">Curs</option>
              <option value="Laborator">Laborator</option>
              <option value="Seminar">Seminar</option>
            </select>
          </div>

          <div className="form-buttons">
            <button 
              type="submit" 
              className="btn save-btn"
              disabled={useCustomSubject && !customSubject.trim() || !useCustomSubject && !selectedSubject}
            >
              Add
            </button>
            <button
              type="button"
              className="btn cancel-btn"
              onClick={() => setSubjectFormVisible(false)}
            >
              Cancel
            </button>
          </div>
        </form>
      </div>
    );
  };

  // ---------------------------------------------------
  // RENDER GENERAL
  // ---------------------------------------------------
  return (
    <div className="admin-page">
      <NavigationHeader 
        userRole="ROLE_ADMIN" 
        userName="Admin"
        onLogout={onLogout}
      />

      <div className="admin-content">
        <hr />
        <h1>Students Management</h1>
        {viewMode === "list" && renderStudentList()}
        {(viewMode === "add" || viewMode === "update") && renderStudentForm()}

        <hr />
        <h1>Professors Management</h1>
        {viewModeProf === "list" && renderProfessorList()}
        {(viewModeProf === "add" || viewModeProf === "update") &&
          renderProfessorForm()}

        {/*
          Formularul pentru a asocia o materie la un profesor (RepartizareProf).
          E ascuns până când user-ul dă "Add Subject" la un profesor.
        */}
        {renderSubjectForm()}
      </div>
    </div>
  );
}

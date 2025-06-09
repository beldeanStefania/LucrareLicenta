import React, { useEffect, useState } from "react";
import { request } from "../helpers/axios-helper";
import NavigationHeader from "./NavigationHeader"; 
import "./AdminPage.css";
import axios from "axios";
import ChatWidget from "./ChatWidget";
import "../styles/pages.css";


export default function AdminPage({ onLogout }) {

  const [adminUser, setAdminUser] = useState(null);
  const [todos, setTodos]       = useState([]);
  const [newTitle, setNewTitle] = useState("");
  const [newDescription, setNewDescription] = useState("");
  const [newDeadline, setNewDeadline]       = useState("");
  const [loadingTodos, setLoadingTodos]     = useState(false);

  const [specializations, setSpecializations] = useState([]);
  const [studentImportReport, setStudentImportReport] = useState([]);

  // ---------------------------
  // STUDENT LOGIC
  // ---------------------------
  const [students, setStudents] = useState([]);
  const [viewMode, setViewMode] = useState("list"); 
  const [studentToEdit, setStudentToEdit] = useState(null);
  const [email, setEmail] = useState(""); 

  // ---------------------------
  // PROFESSOR LOGIC
  // ---------------------------
  const [professors, setProfessors] = useState([]);
  const [viewModeProf, setViewModeProf] = useState("list"); 
  const [professorToEdit, setProfessorToEdit] = useState(null);
  const [professorEmail, setProfessorEmail] = useState(""); // Pentru email-ul profesorului
  const [professorImportReport, setProfessorImportReport] = useState([]);

  // ---------------------------
  // SUBJECT/REPARTIZARE LOGIC
  // ---------------------------
  const [subjectFormVisible, setSubjectFormVisible] = useState(false);
  const [selectedProfForSubject, setSelectedProfForSubject] = useState(null);
  const [allSubjects, setAllSubjects] = useState([]);
  
  // ---------------------------
  // DIRECT SUBJECT MANAGEMENT
  // ---------------------------
  const [viewModeSubject, setViewModeSubject] = useState("list");
  const [subjectToEdit, setSubjectToEdit] = useState(null);
  const [subjectImportReport, setSubjectImportReport] = useState([]);

  useEffect(() => {
    request("GET", "/api/auth/userInfo")
      .then(res => {
        setAdminUser(res.data.username);
        fetchTodos(res.data.username);
      })
      .catch(err => console.error("Nu am userInfo:", err));
  }, []);

   const fetchTodos = (username) => {
    setLoadingTodos(true);
    request("GET", `/api/todo/user/${username}`)
      .then(res => setTodos(res.data || []))
      .catch(err => {
        console.error("Eroare la fetchTodos:", err);
        setTodos([]);
      })
      .finally(() => setLoadingTodos(false));
  };

  const handleAddTodo = () => {
    if (!newTitle || !newDeadline || !adminUser) return;
    request("POST", "/api/todo/create", {
      username:    adminUser,
      title:       newTitle,
      description: newDescription,
      deadline:    newDeadline
    })
    .then(() => {
      setNewTitle(""); setNewDescription(""); setNewDeadline("");
      fetchTodos(adminUser);
    })
    .catch(err => {
      console.error("Eroare la crearea todo:", err);
      alert("Nu am putut crea To-Do.");
    });
  };

   const handleMarkDone = (id) => {
    request("PUT", `/api/todo/update/${id}`, { done: true })
      .then(() => fetchTodos(adminUser))
      .catch(console.error);
  };

  const handleDeleteTodo = (id) => {
    request("DELETE", `/api/todo/delete/${id}`)
      .then(() => fetchTodos(adminUser))
      .catch(console.error);
  };

  // Apelez la inițializare
  useEffect(() => {
    if (adminUser) {
      fetchTodos(adminUser);
    }
  }, [adminUser]);


  useEffect(() => {
    fetchStudents();
    fetchProfessors();
    fetchAllSubjects(); 
    fetchSpecializations();
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

  const fetchSpecializations = () => {
    request("GET", "/api/specializare/getAll")
      .then(res => setSpecializations(res.data))
      .catch(err => console.error("Failed to fetch specializari:", err));
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

const handleSaveStudent = (formValues) => {
  // `formValues` este obiectul obținut din FormData
  // conține doar câmpurile din formular: nume, prenume, an, grupa, specializare
  if (viewMode === "add") {
    // Adăugare student nou
    request("POST", "/api/student/add", formValues)
      .then(() => {
        alert("Student added successfully!");
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
  } else {
    // Update student existent
    // studentToEdit conține obiectul complet cu proprietatea `cod`
    if (!studentToEdit || !studentToEdit.cod) {
      alert("Nu a fost selectat un student valid pentru update.");
      return;
    }
    const codPentruUrl = studentToEdit.cod;
    // Construcția endpoint-ului folosind `cod` din studentToEdit
    const endpoint = `/api/student/update/${codPentruUrl}`;

    // Payload-ul chiar trebuie să conțină numai câmpurile editabile
    // pe care backend-ul le așteaptă în StudentDTO (nume, prenume, an, grupă, specializare).
    const payload = {
      nume: formValues.nume,
      prenume: formValues.prenume,
      an: parseInt(formValues.an, 10),
      grupa: formValues.grupa,
      specializare: formValues.specializare
    };

    request("PUT", endpoint, payload)
      .then(() => {
        alert("Student updated successfully!");
        setViewMode("list");
        setStudentToEdit(null);
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
          }
          alert(`Eroare: ${errorMessage}`);
        } else {
          alert("Eroare: Nu s-a putut contacta serverul.");
        }
      });
  }
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
        const formValues = Object.fromEntries(formData.entries());
        handleSaveStudent(formValues);
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
              <div className="form-group">
      <label>Email:</label>
      <input
        type="email"
        name="email"
        defaultValue=""
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
      <div className="form-group">
        <label>Specializare:</label>
        <select
          name="specializare"
          defaultValue={studentToEdit?.specializare?.specializare || ""}
          required
          className="input-field"
        >
          <option value="" disabled>Selectează specializarea</option>
          {specializations.map((s) => (
            <option key={s.id} value={s.specializare}>
              {s.specializare}
            </option>
          ))}
        </select>
      </div>

      <div className="form-buttons">
        <button type="submit" className="btn save-btn">
          {viewMode === "add" ? "Add Student" : "Save"}
        </button>
        <button
          type="button"
          className="btn cancel-btn"
          onClick={() => {
            setViewMode("list");
            setStudentToEdit(null);
          }}
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

  const handleImportProfessors = (e) => {
  const file = e.target.files[0];
  if (!file) return;
  const formData = new FormData();
  formData.append("file", file);

  // folosește wrapper-ul care deja pune Authorization
  request("POST", "/api/profesor/import", formData)
    .then(res => {
      setProfessorImportReport(res.data);
      fetchProfessors();
    })
    .catch(err => {
      console.error("Import error", err);
      alert("Eroare la import. Verifică console pentru detalii.");
    });
};

  const renderProfessorList = () => (
    <div className="list-container">
      <h2>Professors List</h2>
      <button className="btn add-btn" onClick={handleAddProfessor}>
        Add Profesor
      </button>
      <>
  <label className="btn import-btn">
    Import CSV
    <input type="file" accept=".csv"
      onChange={handleImportProfessors}
      style={{ display: 'none' }}
    />
  </label>
      </>
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
                  <td>{prof.user?.email || "N/A"}</td>
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
          handleSaveProfessor(data);
        }}
      >
        <div className="form-group">
          <label>First Name (Prenume):</label>
          <input
            name="nume"
            defaultValue={professorToEdit?.nume || ""}
            required
            className="input-field"
          />
        </div>
        <div className="form-group">
          <label>Last Name (Nume):</label>
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
            required={viewModeProf === "add"} 
            className="input-field"
          />
        </div>
        <div className="form-group">
      <label>Email:</label>
      <input
        type="email"
        name="email"
        defaultValue={professorToEdit?.user?.email || ""}
        required
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

  const [useCustomSubject, setUseCustomSubject] = useState(false);
  const [customSubject, setCustomSubject] = useState("");
  const [selectedSubject, setSelectedSubject] = useState("");

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
    return request("POST", "/api/materie/add", { 
      nume: subjectName,
      semestru: 1,    
      cod: subjectName.toUpperCase().replace(/\s+/g, "") 
    })
      .then(response => {
        fetchAllSubjects(); 
        return response.data;
      });
  };
  

  const handleSubmitSubject = (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    const data = Object.fromEntries(formData.entries());
    
    const subjectName = useCustomSubject ? customSubject : data.materie;
    
    const assignSubject = () => {
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
      createNewSubject(customSubject.trim())
        .then(() => {
          assignSubject();
        })
        .catch(error => {
          console.error("Failed to create new subject:", error);
          alert("Failed to create new subject. Please try again.");
        });
    } else {
      assignSubject();
    }
  };

  const handleImportSubjects = (e) => {
  const file = e.target.files[0];
  if (!file) return;
  const formData = new FormData();
  formData.append("file", file);

  request("POST", "/api/materie/import", formData)
    .then(res => {
      setSubjectImportReport(res.data);
      fetchAllSubjects();   // reîncarcă lista
    })
    .catch(err => {
      console.error("Import error materii", err);
      alert("Eroare la import materii. Verifică console pentru detalii.");
    });
};


  const renderAssignSubjectForm = () => {
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

  // ========== SUBJECTS ==========
  const handleAddSubject = () => {
    setViewModeSubject("add");
    setSubjectToEdit(null);
  };

  const handleEditSubject = (subject) => {
    setViewModeSubject("update");
    setSubjectToEdit(subject);
  };

  const handleDeleteSubject = (id) => {
    if (window.confirm("Are you sure you want to delete this subject?")) {
      request("DELETE", `/api/materie/delete/${id}`)
        .then(() => {
          alert("Subject deleted successfully!");
          fetchAllSubjects();
        })
        .catch((error) => {
          console.error("Failed to delete subject:", error);
          alert("Failed to delete subject. Please try again.");
        });
    }
  };

  const handleSaveSubject = (subject) => {
    const method = viewModeSubject === "add" ? "POST" : "PUT";
    const endpoint = viewModeSubject === "add" ? "/api/materie/add" : "/api/materie/update";

    request(method, endpoint, subject)
      .then(() => {
        alert(viewModeSubject === "add" ? "Subject added successfully!" : "Subject updated successfully!");
        setViewModeSubject("list");
        fetchAllSubjects();
      })
      .catch((error) => {
        console.error("Failed to save subject:", error);
        alert("Failed to save subject. Please try again.");
      });
  };

  const renderSubjectList = () => (
    <div className="list-container">
      <h2>Subjects List</h2>
      <button className="btn add-btn" onClick={handleAddSubject}>
        Add Subject
      </button>
      <>
  <label className="btn import-btn">
    Import CSV
    <input type="file" accept=".csv"
      onChange={handleImportSubjects}
      style={{ display: 'none' }}
    />
  </label>
  {subjectImportReport.length > 0 && (
    <div className="import-report">
      <h3>Raport import:</h3>
      <ul>
        {subjectImportReport.map(r =>
          <li key={r.row} style={{ color: r.success ? 'green' : 'red' }}>
            Linie {r.row}: {r.success ? 'OK' : r.message}
          </li>
        )}
      </ul>
    </div>
  )}
</>

      {allSubjects.length > 0 ? (
        <div className="scroll-container">
          <table className="students-table">
            <thead>
              <tr>
                <th>Name</th>
                <th>Code</th>
                <th>Semester</th>
                <th>Credits</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {allSubjects.map((subject) => (
                <tr key={subject.id}>
                  <td>{subject.nume}</td>
                  <td>{subject.cod}</td>
                  <td>{subject.semestru}</td>
                  <td>{subject.credite}</td>
                  <td>
                    <button className="btn edit-btn" onClick={() => handleEditSubject(subject)}>Edit</button>
                    <button className="btn delete-btn" onClick={() => handleDeleteSubject(subject.id)}>Delete</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ) : (
        <p>No subjects available</p>
      )}
    </div>
  );

  const renderSubjectForm = () => (
    <div className="form-container">
      <h2>{viewModeSubject === "add" ? "Add Subject" : "Edit Subject"}</h2>
      <form
        onSubmit={(e) => {
          e.preventDefault();
          const formData = new FormData(e.target);
          const subject = Object.fromEntries(formData.entries());
          // Convert string values to numbers where needed
          subject.semestru = parseInt(subject.semestru, 10);
          subject.credite = parseInt(subject.credite, 10);
          handleSaveSubject(subject);
        }}
      >
        <div className="form-group">
          <label>Name:</label>
          <input
            name="nume"
            defaultValue={subjectToEdit?.nume || ""}
            required
            className="input-field"
          />
        </div>
        <div className="form-group">
          <label>Code:</label>
          <input
            name="cod"
            defaultValue={subjectToEdit?.cod || ""}
            required
            className="input-field"
          />
        </div>
        <div className="form-group">
          <label>Semester:</label>
          <input
            type="number"
            name="semestru"
            defaultValue={subjectToEdit?.semestru || "1"}
            required
            min="1"
            max="2"
            className="input-field"
          />
        </div>
        <div className="form-group">
          <label>Credits:</label>
          <input
            type="number"
            name="credite"
            defaultValue={subjectToEdit?.credite || ""}
            required
            min="1"
            className="input-field"
          />
        </div>
        <div className="form-buttons">
          <button type="submit" className="btn save-btn">
            {viewModeSubject === "add" ? "Add Subject" : "Save"}
          </button>
          <button
            type="button"
            className="btn cancel-btn"
            onClick={() => setViewModeSubject("list")}
          >
            Cancel
          </button>
        </div>
      </form>
    </div>
  );

  return (
    <>
      <div className="admin-page">
        <NavigationHeader 
          userRole="ROLE_ADMIN" 
          userName={adminUser || ""}
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
            
          <hr />
          <h1>Subjects Management</h1>
          {viewModeSubject === "list" && renderSubjectList()}
          {(viewModeSubject === "add" || viewModeSubject === "update") && renderSubjectForm(a)}

          {renderAssignSubjectForm()}
           <hr />
        {/* To-Do Management */}
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
        onChange={e => setNewTitle(e.target.value)}
      />
      <input
        type="text"
        placeholder="Descriere (opțional)"
        value={newDescription}
        onChange={e => setNewDescription(e.target.value)}
      />
      <input
        type="date"
        value={newDeadline}
        onChange={e => setNewDeadline(e.target.value)}
      />
      <button className="save-btn" onClick={handleAddTodo}>
        Adaugă
      </button>
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
                      className="save-btn"
                      onClick={() => handleMarkDone(todo.id)}
                      style={{ marginRight: "8px" }}
                    >
                      Marchează ca done
                    </button>
                  )}
                  <button
                    className="save-btn"
                    onClick={() => handleDeleteTodo(todo.id)}
                  >
                    Șterge
                  </button>
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
        <ChatWidget />
        </div>
      </div>
    </>
  );
}

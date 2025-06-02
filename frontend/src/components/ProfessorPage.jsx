import React, { useEffect, useState } from "react";
import { request } from "../helpers/axios-helper";
import NavigationHeader from "./NavigationHeader";
//import "../styles/pages.css";
import "./ProfessorPage.css"; // Assuming you have a CSS file for styling
import ChatWidget from "./ChatWidget"; // Importing the ChatWidget component

export default function ProfessorPage({ onLogout }) {
const [todos, setTodos] = useState([]);
  const [newTodo, setNewTodo] = useState({ title: "", description: "", deadline: "" });


  const [professorInfo, setProfessorInfo] = useState(null);

  const [repartizari, setRepartizari] = useState([]);

  const [cladiri, setCladiri] = useState([]);
  const [selectedCladire, setSelectedCladire] = useState("");
  const [rooms, setRooms] = useState([]);
  const [selectedRoom, setSelectedRoom] = useState("");

  const [materiiUnice, setMateriiUnice] = useState([]); 
  const [selectedGradeMaterie, setSelectedGradeMaterie] = useState("");
  const [selectedGradeGroup, setSelectedGradeGroup] = useState("");
  const [gradeStudentNotes, setGradeStudentNotes] = useState({});
  const [students, setStudents] = useState([]);

  const [newTitle, setNewTitle] = useState("");
  const [newDescription, setNewDescription] = useState("");
  const [newDeadline, setNewDeadline] = useState("");
  const [loadingTodos, setLoadingTodos] = useState(true);

  const [selectedMaterieForSchedule, setSelectedMaterieForSchedule] = useState("");
  const [selectedTip, setSelectedTip] = useState("");
  const [selectedFrecventa, setSelectedFrecventa] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [scheduleData, setScheduleData] = useState({
    grupa: "",
    oraInceput: "",
    oraSfarsit: "",
    zi: "",
    semigrupa: "",
  });

  const [profSchedule, setProfSchedule] = useState([]);  

const fetchTodos = (username) => {
  setLoadingTodos(true);
  request("GET", `/api/todo/user/${username}`)
    .then((res) => {
      setTodos(res.data || []);
    })
    .catch((err) => {
      console.error("Eroare la fetchTodos:", err);
      setTodos([]);
    })
    .finally(() => setLoadingTodos(false));
};

const handleAddTodo = () => {
  if (!newTitle || !newDeadline) return;

  const payload = {
    username: professorInfo?.username,
    title: newTitle,
    description: newDescription,
    deadline: newDeadline,
  };

  request("POST", "/api/todo/create", payload)
    .then(() => {
      setNewTitle("");
      setNewDescription("");
      setNewDeadline("");
      fetchTodos(professorInfo?.username);
    })
    .catch((err) => {
      console.error("Eroare la adăugare todo:", err);
      alert("Eroare la crearea To-Do-ului!");
    });
};

const handleMarkDone = (todoId) => {
  const todo = todos.find((t) => t.id === todoId);
  if (!todo) return;

  const updatedTodo = {
    title: todo.title,
    description: todo.description,
    deadline: todo.deadline,
    done: true,
  };

  request("PUT", `/api/todo/update/${todoId}`, updatedTodo)
    .then(() => fetchTodos(professorInfo?.username))
    .catch((err) => console.error("Eroare la marcare todo ca done:", err));
};

const handleDeleteTodo = (todoId) => {
  request("DELETE", `/api/todo/delete/${todoId}`)
    .then(() => fetchTodos(professorInfo?.username))
    .catch((err) => console.error(err));
};

useEffect(() => {
  if (professorInfo?.username) {
    fetchTodos(professorInfo.username);
  }
}, [professorInfo]);


  useEffect(() => {
    fetchProfessorInfo();
    fetchCladiri();
  }, []);

  useEffect(() => {
    if (professorInfo && professorInfo.profesorId) {
      fetchMateriiProfesor(professorInfo.profesorId);
      fetchProfessorSchedule(professorInfo.profesorId);
    }
  }, [professorInfo]);

  useEffect(() => {
    if (selectedGradeGroup) {
      fetchStudentsByGroup(selectedGradeGroup);
    }
  }, [selectedGradeGroup]);

  const fetchProfessorInfo = () => {
    request("GET", "/api/auth/userInfo")
      .then((response) => {
        setProfessorInfo(response.data);
      })
      .catch((error) => console.error("Failed to fetch professor info:", error));
  };

  const fetchMateriiProfesor = (profesorId) => {
    request("GET", `/api/repartizareProf/materiiProfesor/${profesorId}`)
      .then((response) => {
        if (response.data && response.data.length > 0) {
          setRepartizari(response.data);
  
          const materiiUnice = Array.from(
            new Set(response.data.map((item) => item.materie))
          ).map((materie) => ({
            denumire: materie, 
          }));
  
          setMateriiUnice(materiiUnice);
        } else {
          setRepartizari([]);
          setMateriiUnice([]);
          console.log("Nicio materie disponibilă pentru acest profesor.");
        }
      })
      .catch((error) => {
        console.error("Failed to fetch materiile profesorului:", error);
        setRepartizari([]);
        setMateriiUnice([]);
      });
  };

  const fetchCladiri = () => {
    request("GET", "/api/cladire/getAll")
      .then((response) => {
        setCladiri(response.data);
      })
      .catch((error) => console.error("Failed to fetch cladiri:", error));
  };

  const fetchRoomsByCladire = (cladireId) => {
    request("GET", `/api/sala/byCladire/${cladireId}`)
      .then((response) => {
        setRooms(response.data);
      })
      .catch((error) => console.error("Failed to fetch rooms:", error));
  };

  const fetchStudentsByGroup = (grupa) => {
    request("GET", `/api/student/getByGrupa/${grupa}`)
      .then((response) => {
        const data = Array.isArray(response.data) ? response.data : [response.data];
        setStudents(data);
      })
      .catch((error) => {
        console.error("Failed to fetch students by group:", error);
        setStudents([]);
      });
  };

  const fetchProfessorSchedule = (profesorId) => {
    request("GET", `/api/orare/getAllProfesor/${profesorId}`)
      .then((response) => {
        setProfSchedule(response.data);
      })
      .catch((error) => {
        console.error("Failed to fetch professor schedule:", error);
        setProfSchedule([]);
      });
  };

  const handleAddGrades = () => {
    const studentEntries = Object.entries(gradeStudentNotes);
    
    if (studentEntries.length === 0) {
      alert("Nu există note de adăugat!");
      return;
    }
  
    const requests = studentEntries.map(([studentCod, nota]) => {
      const payload = {
        studentCod,
        numeMaterie: selectedGradeMaterie,
        nota: parseFloat(nota),
        semestru: 1,
      };
  
      return request("POST", "/api/catalogStudentMaterie/add", payload)
        .then(() => ({ success: true, studentCod, nota }))
        .catch((error) => ({ success: false, studentCod, error }));
    });
  
    Promise.all(requests).then((results) => {
      const succesuri = results.filter((res) => res.success);
      const erori = results.filter((res) => !res.success);
  
      if (succesuri.length > 0) {
        if (succesuri.length === 1) {
          alert(
            `Nota ${succesuri[0].nota} a fost adăugată pentru studentul ${succesuri[0].studentCod}`
          );
        } else {
          alert(`Notele au fost adăugate pentru ${succesuri.length} studenți.`);
        }
      }
  
      if (erori.length > 0) {
        console.error("Erori la adăugarea notelor:", erori);
        alert(`Eroare la adăugarea notelor pentru ${erori.length} studenți.`);
      }
    });
  };
  
  const handleReserveRoom = () => {
    if (!selectedMaterieForSchedule || !selectedTip || !selectedCladire || !selectedRoom || !scheduleData.zi || !scheduleData.oraInceput || !scheduleData.oraSfarsit) {
      setErrorMessage("Toate câmpurile trebuie completate pentru a rezerva sala!");
      return;
    }
  
    setErrorMessage(""); 
  
    const payload = {
      grupa: scheduleData.grupa,
      oraInceput: parseInt(scheduleData.oraInceput.split(":")[0], 10),
      oraSfarsit: parseInt(scheduleData.oraSfarsit.split(":")[0], 10),
      zi: scheduleData.zi,
      profesorId: professorInfo.profesorId,
      materie: selectedMaterieForSchedule,
      tip: selectedTip,
      salaId: selectedRoom,
      frecventa: selectedTip === "Laborator" || selectedTip === "Seminar" ? selectedFrecventa : "",
      semigrupa: selectedTip === "Laborator" ? scheduleData.semigrupa : null,
    };
  
    console.log("Rezervare sală payload:", payload);
  
    request("POST", "/api/orare/add", payload)
      .then(() => alert("Sală rezervată cu succes!"))
      .catch((error) => console.error("Failed to reserve room:", error));
  };
  
  const handleCladireChange = (cladireId) => {
    setSelectedCladire(cladireId);
    fetchRoomsByCladire(cladireId);
  };

  return (
    <div className="professor-page">
      <NavigationHeader 
        userRole="ROLE_PROFESOR" 
        userName={professorInfo?.username}
        onLogout={onLogout}
      />

      <div className="professor-content">
         <ChatWidget />
        {}
        <section>
          <h2>Acordă note studenților</h2>
          <div>
            <label>Materie:</label>
            <select
              onChange={(e) => setSelectedGradeMaterie(e.target.value)}
              value={selectedGradeMaterie}
            >
              <option value="">Selectează materie</option>
              {materiiUnice.map((materie) => (
                <option key={materie.denumire} value={materie.denumire}>
                  {materie.denumire}
                </option>
              ))}
            </select>
          </div>
          <div>
            <label>Grupa studenților:</label>
            <input
              type="text"
              placeholder="Introdu grupa"
              onChange={(e) => setSelectedGradeGroup(e.target.value)}
            />
          </div>
          <div className="students-list">
            {students && students.length > 0 ? (
              students.map((student) => (
                <div key={student.cod} className="student-entry">
                  <span className="student-name">{student.nume} {student.prenume}</span>
                  <input
                    type="number"
                    placeholder="Introdu nota"
                    value={gradeStudentNotes[student.cod] || ""}
                    onChange={(e) =>
                      setGradeStudentNotes({
                        ...gradeStudentNotes,
                        [student.cod]: e.target.value,
                      })
                    }
                  />
                </div>
              ))
            ) : (
              <p>Nu au fost găsiți studenți pentru grupa specificată.</p>
            )}
            
          </div>


          {errorMessage && <p className="error-message">{errorMessage}</p>}
          <button onClick={handleAddGrades}>Adaugă Note</button>
        </section>

        {}
        <section>
          <h2>Rezervare sală</h2>
          <div>
            <label>Materie:</label>
            <select
              onChange={(e) => setSelectedMaterieForSchedule(e.target.value)}
              value={selectedMaterieForSchedule}
            >
              <option value="">Selectează materie</option>
              {materiiUnice.map((materie, index) => (
                <option key={materie.denumire} value={materie.denumire}>
                  {materie.denumire}
                </option>
              ))}
            </select>
          </div>
          <div>
            <label>Tip:</label>
            <select
              onChange={(e) => setSelectedTip(e.target.value)}
              value={selectedTip}
            >
              <option value="">Selectează tipul</option>
              <option value="Curs">Curs</option>
              <option value="Laborator">Laborator</option>
              <option value="Seminar">Seminar</option>
            </select>
          </div>
          {(selectedTip === "Laborator" || selectedTip === "Seminar") && (
            <div>
              <label>Frecvența:</label>
              <select
                onChange={(e) => setSelectedFrecventa(e.target.value)}
                value={selectedFrecventa}
              >
                <option value="">Selectează frecvența</option>
                <option value="1 sapt">1 săptămână</option>
                <option value="2 sapt">2 săptămâni</option>
                <option value="saptamanal">Săptămânal</option>
              </select>
            </div>
          )}
          {selectedTip === "Laborator" && (
            <div>
              <label>Semigrupa:</label>
              <input
                type="text"
                placeholder="ex: 1 sau 2"
                onChange={(e) =>
                  setScheduleData({ ...scheduleData, semigrupa: e.target.value })
                }
              />
            </div>
          )}
          <div>
            <label>Clădire:</label>
            <select
              onChange={(e) => handleCladireChange(e.target.value)}
              value={selectedCladire}
            >
              <option value="">Selectează clădirea</option>
              {cladiri.map((cladire) => (
                <option key={cladire.id} value={cladire.id}>
                  {cladire.nume}
                </option>
              ))}
            </select>
          </div>
          <div>
            <label>Sală:</label>
            <select
              onChange={(e) => setSelectedRoom(e.target.value)}
              value={selectedRoom}
            >
              <option value="">Selectează sala</option>
              {rooms.map((room) => (
                <option key={room.id} value={room.id}>
                  {room.nume}
                </option>
              ))}
            </select>
          </div>
          <div>
            <label>Grupa:</label>
            <input
              type="text"
              placeholder="Introduceți grupa"
              onChange={(e) =>
                setScheduleData({ ...scheduleData, grupa: e.target.value })
              }
            />
          </div>
          <div>
            <label>Ora început:</label>
            <input
              type="time"
              onChange={(e) =>
                setScheduleData({ ...scheduleData, oraInceput: e.target.value })
              }
            />
          </div>
          <div>
            <label>Ora sfârșit:</label>
            <input
              type="time"
              onChange={(e) =>
                setScheduleData({ ...scheduleData, oraSfarsit: e.target.value })
              }
            />
          </div>
          <div>
            <label>Ziua:</label>
            <select
              onChange={(e) =>
                setScheduleData({ ...scheduleData, zi: e.target.value })
              }
              value={scheduleData.zi}
            >
              <option value="">Selectează ziua</option>
              <option value="Luni">Luni</option>
              <option value="Marti">Marți</option>
              <option value="Miercuri">Miercuri</option>
              <option value="Joi">Joi</option>
              <option value="Vineri">Vineri</option>
            </select>
          </div>
          {errorMessage && <p className="error-message">{errorMessage}</p>}
          <button onClick={handleReserveRoom}>Rezervă Sală</button>
        </section>

        {}
        <section>
          <h2>Orarul meu</h2>
          {profSchedule.length > 0 ? (
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
                </tr>
              </thead>
              <tbody>
                {profSchedule.map((item, index) => (
                  <tr key={index}>
                    <td>{item.zi}</td>
                    <td>{`${item.oraInceput}:00 - ${item.oraSfarsit}:00`}</td>
                    <td>{item.frecventa === "saptamanal" ? "" : item.frecventa}</td>
                    <td>{item.sala}</td>
                    <td>{item.tipul}</td>
                    <td>{item.formatia}</td>
                    <td>{item.disciplina}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          ) : (
            <p>Nicio oră planificată pentru acest profesor.</p>
          )}
        </section>
        <section id="todo">
  <h2>To-Do List</h2>

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

  {loadingTodos ? (
    <p>Se încarcă To-Do-urile...</p>
  ) : todos.length > 0 ? (
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
        {todos.map((todo) => (
          <tr key={todo.id}>
            <td>{todo.title}</td>
            <td>{todo.description || "—"}</td>
            <td>{todo.deadline}</td>
            <td>{todo.done ? "Finalizat" : "Nefinalizat"}</td>
            <td>
              {!todo.done && (
                <button onClick={() => handleMarkDone(todo.id)}> Done</button>
              )}
              <button onClick={() => handleDeleteTodo(todo.id)}>Sterge </button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  ) : (
    <p>Nu ai niciun To-Do momentan.</p>
  )}
</section>

      </div>
    </div>
  );
}

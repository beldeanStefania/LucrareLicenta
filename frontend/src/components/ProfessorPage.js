import React, { useEffect, useState } from "react";
import { request } from "../helpers/axios-helper";
import "./ProfessorPage.css";

export default function ProfessorPage({ onLogout }) {
  // State-uri pentru informațiile profesorului, materii, repartizări, clădiri și săli
  const [professorInfo, setProfessorInfo] = useState(null);
  const [repartizari, setRepartizari] = useState([]);
  const [materiiUnice, setMateriiUnice] = useState([]);
  const [cladiri, setCladiri] = useState([]);
  const [rooms, setRooms] = useState([]);
  const [selectedCladire, setSelectedCladire] = useState("");
  const [selectedRoom, setSelectedRoom] = useState("");
  const [selectedTip, setSelectedTip] = useState("");
  const [selectedFrecventa, setSelectedFrecventa] = useState("");
  const [schedule, setSchedule] = useState({
    grupa: "",
    oraInceput: "",
    oraSfarsit: "",
    zi: "",
    semigrupa: "",
  });

  // State-uri dedicate secțiunii de acordare a notelor
  const [selectedGradeMaterie, setSelectedGradeMaterie] = useState("");
  const [selectedGradeGroup, setSelectedGradeGroup] = useState("");
  const [gradeStudentNotes, setGradeStudentNotes] = useState({});
  const [students, setStudents] = useState([]); // Listă studenți pentru grupa selectată

  // La montare, se preiau informațiile profesorului și clădirile
  useEffect(() => {
    fetchProfessorInfo();
    fetchCladiri();
  }, []);

  // Când avem informațiile profesorului, preluăm materii/repartizări
  useEffect(() => {
    if (professorInfo && professorInfo.profesorId) {
      fetchMateriiProfesor(professorInfo.profesorId);
    }
  }, [professorInfo]);

  // Când se selectează o grupă pentru notare, preluăm studenții aferenți
  useEffect(() => {
    if (selectedGradeGroup) {
      fetchStudentsByGroup(selectedGradeGroup);
    }
  }, [selectedGradeGroup]);

  // Funcție pentru a prelua informațiile profesorului
  const fetchProfessorInfo = () => {
    request("GET", "/api/auth/userInfo")
      .then((response) => {
        setProfessorInfo(response.data);
      })
      .catch((error) => console.error("Failed to fetch professor info:", error));
  };

  // Funcție pentru a prelua materiile/repartizările profesorului
  const fetchMateriiProfesor = (profesorId) => {
    request("GET", `/api/repartizareProf/materiiProfesor/${profesorId}`)
      .then((response) => {
        if (response.data && response.data.length > 0) {
          // Salvează o listă cu obiecte care conțin codul și denumirea materiei
          const materiiUnice = response.data.map((item) => ({
            cod: item.codMaterie, // Codul materiei (ex: MLR5000)
            denumire: item.materie, // Numele complet al materiei
          }));
          setMateriiUnice(materiiUnice);
        } else {
          console.log("Nicio materie disponibilă pentru acest profesor.");
          setMateriiUnice([]);
        }
      })
      .catch((error) => {
        console.error("Failed to fetch materiile profesorului:", error);
        setMateriiUnice([]);
      });
  };
  

  // Funcție pentru a prelua clădirile disponibile
  const fetchCladiri = () => {
    request("GET", "/api/cladire/getAll")
      .then((response) => {
        setCladiri(response.data);
      })
      .catch((error) => console.error("Failed to fetch cladiri:", error));
  };

  // Funcție pentru a prelua sălile pentru o anumită clădire
  const fetchRoomsByCladire = (cladireId) => {
    request("GET", `/api/sala/byCladire/${cladireId}`)
      .then((response) => {
        setRooms(response.data);
      })
      .catch((error) => console.error("Failed to fetch rooms:", error));
  };

  // Funcție pentru a prelua studenții după grupa selectată în secțiunea de note
  const fetchStudentsByGroup = (grupa) => {
    request("GET", `/api/student/getByGrupa/${grupa}`)
      .then((response) => {
        // Se presupune că endpointul întoarce un array de studenți
        setStudents(Array.isArray(response.data) ? response.data : [response.data]);
      })
      .catch((error) => {
        console.error("Failed to fetch students by group:", error);
        setStudents([]);
      });
  };

  // Funcție pentru a trimite notele acordate studenților
  const handleAddGrades = () => {
    // Parcurgi "gradeStudentNotes" care arată { "codStudent1": 9, "codStudent2": 7, ...}
    Object.entries(gradeStudentNotes).forEach(([studentCod, nota]) => {
      const payload = {
        studentCod,
        codMaterie: selectedGradeMaterie, // ex. "MLR5000"
        nota: parseFloat(nota),
        semestru: 1,
      };

      console.log("payload trimis:", payload);

      request("POST", "/api/catalogStudentMaterie/add", payload)
        .then(() => {
          alert(`Nota ${nota} a fost adăugată pentru studentul ${studentCod}`);
        })
        .catch((error) => console.error("Failed to add grade:", error));
    });
  };
  
  // Funcție pentru a trimite cererea de rezervare a sălii
  const handleReserveRoom = () => {
    const repartizare = repartizari.find(
      (r) =>
        r.materie === selectedGradeMaterie &&
        r.tip.toUpperCase() === selectedTip.toUpperCase()
    );

    request("POST", "/api/orare/add", {
      grupa: schedule.grupa,
      oraInceput: parseInt(schedule.oraInceput.split(":")[0], 10),
      oraSfarsit: parseInt(schedule.oraSfarsit.split(":")[0], 10),
      zi: schedule.zi,
      repartizareProfId: repartizare?.id || null,
      profesorId: professorInfo.profesorId,
      materie: selectedGradeMaterie, // folosim materia selectată pentru notare (poate fi reutilizată)
      tip: selectedTip,
      salaId: selectedRoom,
      frecventa:
        selectedTip === "Laborator" || selectedTip === "Seminar"
          ? selectedFrecventa
          : "",
      semigrupa: selectedTip === "Laborator" ? schedule.semigrupa : null,
    })
      .then(() => alert("Sală rezervată cu succes!"))
      .catch((error) => console.error("Failed to reserve room:", error));
  };

  // Funcție pentru schimbarea clădirii (și preluarea sălilor aferente)
  const handleCladireChange = (cladireId) => {
    setSelectedCladire(cladireId);
    fetchRoomsByCladire(cladireId);
  };

  return (
    <div className="professor-page">
      <header>
        <h1>Bun venit!</h1>
        <button className="logout-btn" onClick={onLogout}>
          Logout
        </button>
      </header>

      {/* Secțiunea de acordare a notelor */}
      <section>
  <h2>Acordă note studenților</h2>
  <div>
    <label>Materie pentru note:</label>
    <select
      onChange={(e) => setSelectedGradeMaterie(e.target.value)}
      value={selectedGradeMaterie}
    >
      <option value="">Selectează materie</option>
      {materiiUnice.map((materie, index) => (
        <option key={index} value={materie.denumire}>
          {materie.denumire}
        </option>
      ))}
    </select>
  </div>
  <div>
    <label>Grupa studenților:</label>
    <input
      type="text"
      placeholder="Introdu grupa pentru note"
      onChange={(e) => setSelectedGradeGroup(e.target.value)}
    />
  </div>
  <div className="students-list">
    {students && students.length > 0 ? (
      students.map((student) => (
        <div key={student.cod}>
          <span>
            {student.nume} {student.prenume}
          </span>
          <input
            type="number"
            placeholder="Introdu nota"
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
  <button onClick={handleAddGrades}>Adaugă Note</button>
</section>


      {/* Secțiunea pentru rezervarea sălii */}
      <section>
        <h2>Rezervare sală</h2>
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
          <label>Grupa pentru rezervare:</label>
          <input
            type="text"
            placeholder="Introdu grupa"
            onChange={(e) =>
              setSchedule({ ...schedule, grupa: e.target.value })
            }
          />
        </div>
        <div>
          <label>Ora început:</label>
          <input
            type="time"
            onChange={(e) =>
              setSchedule({ ...schedule, oraInceput: e.target.value })
            }
          />
        </div>
        <div>
          <label>Ora sfârșit:</label>
          <input
            type="time"
            onChange={(e) =>
              setSchedule({ ...schedule, oraSfarsit: e.target.value })
            }
          />
        </div>
        <div>
          <label>Ziua:</label>
          <select
            onChange={(e) =>
              setSchedule({ ...schedule, zi: e.target.value })
            }
            value={schedule.zi}
          >
            <option value="">Selectează ziua</option>
            <option value="Luni">Luni</option>
            <option value="Marti">Marți</option>
            <option value="Miercuri">Miercuri</option>
            <option value="Joi">Joi</option>
            <option value="Vineri">Vineri</option>
          </select>
        </div>
        {selectedTip && (
          <>
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
                  placeholder="Introdu semigrupa (ex: 1 sau 2)"
                  onChange={(e) =>
                    setSchedule({ ...schedule, semigrupa: e.target.value })
                  }
                />
              </div>
            )}
          </>
        )}
        <button onClick={handleReserveRoom}>Rezervă Sală</button>
      </section>
    </div>
  );
}

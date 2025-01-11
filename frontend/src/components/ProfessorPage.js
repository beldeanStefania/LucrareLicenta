import React, { useEffect, useState } from "react";
import { request } from "../helpers/axios-helper";
import "./ProfessorPage.css";

export default function ProfessorPage({ onLogout }) {
  const [professorInfo, setProfessorInfo] = useState(null); // Info despre profesor
  const [repartizari, setRepartizari] = useState([]); // Materii și activități repartizate
  const [students, setStudents] = useState([]); // Lista studenților
  const [selectedMaterie, setSelectedMaterie] = useState("");
  const [studentNotes, setStudentNotes] = useState({}); // Note introduse pentru studenți
  const [cladiri, setCladiri] = useState([]); // Clădiri disponibile
  const [selectedCladire, setSelectedCladire] = useState(""); // Clădire selectată
  const [rooms, setRooms] = useState([]); // Săli disponibile
  const [selectedTip, setSelectedTip] = useState(""); // Tipul activității (ex. Curs, Laborator)
  const [selectedFrecventa, setSelectedFrecventa] = useState(""); // Frecvența activității
  const [materiiUnice, setMateriiUnice] = useState([]);
  const [selectedRoom, setSelectedRoom] = useState("");
  const [schedule, setSchedule] = useState({
    grupa: "",
    oraInceput: "",
    oraSfarsit: "",
    zi: "",
  });

  // Fetch professor info, repartizari, and cladiri
  useEffect(() => {
    fetchProfessorInfo();
    fetchCladiri();
  }, []);

  useEffect(() => {
    if (professorInfo && professorInfo.profesorId) {
      console.log("Fetching materii for profesorId:", professorInfo.profesorId);
      fetchMateriiProfesor(professorInfo.profesorId);
    }
  }, [professorInfo]); // Se execută când professorInfo este actualizat

  const fetchProfessorInfo = () => {
    request("GET", "/api/auth/userInfo")
      .then((response) => {
        console.log("Professor info:", response.data);
        setProfessorInfo(response.data); // Setăm informațiile despre profesor
      })
      .catch((error) =>
        console.error("Failed to fetch professor info:", error)
      );
  };

  const fetchMateriiProfesor = (profesorId) => {
    request("GET", `/api/repartizareProf/materiiProfesor/${profesorId}`)
      .then((response) => {
        if (response.data && response.data.length > 0) {
          // Eliminăm duplicatele pe baza numelui materiei
          const materiiUnice = Array.from(
            new Set(response.data.map((item) => item.materie))
          );
  
          setRepartizari(response.data);
          console.log("Materii unice:", materiiUnice);
          setMateriiUnice(materiiUnice); // Setăm materiile unice în state
        } else {
          console.log("Nicio materie disponibilă pentru acest profesor.");
          setRepartizari([]);
        }
      })
      .catch((error) => {
        console.error("Failed to fetch materiile profesorului:", error);
        setRepartizari([]);
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

  const handleAddNotes = () => {
    Object.entries(studentNotes).forEach(([studentCod, nota]) => {
      request("POST", "/api/catalogStudentMaterie/add", {
        studentCod,
        codMaterie: selectedMaterie,
        nota,
        semestru: 1, // Semestrul poate fi selectat sau hardcodat
      })
        .then(() =>
          alert(`Nota ${nota} adăugată pentru student ${studentCod}`)
        )
        .catch((error) => console.error("Failed to add grade:", error));
    });
  };

  const handleReserveRoom = () => {
    const repartizare = repartizari.find(
      (r) =>
        r.materie === selectedMaterie &&
        r.tip.toUpperCase() === selectedTip.toUpperCase()
    );
  
    request("POST", "/api/orare/add", {
      grupa: schedule.grupa,
      oraInceput: parseInt(schedule.oraInceput.split(":")[0], 10),
      oraSfarsit: parseInt(schedule.oraSfarsit.split(":")[0], 10),
      zi: schedule.zi,
      repartizareProfId: repartizare?.id || null,
      profesorId: professorInfo.profesorId,
      materie: selectedMaterie,
      tip: selectedTip,
      salaId: selectedRoom,
      frecventa:
        selectedTip === "Laborator" || selectedTip === "Seminar"
          ? selectedFrecventa
          : "",
      semigrupa: selectedTip === "Laborator" ? schedule.semigrupa : null, // Trimite semigrupa doar pentru laborator
    })
      .then(() => alert("Sală rezervată cu succes!"))
      .catch((error) => console.error("Failed to reserve room:", error));
  };
  
  
  

  const handleCladireChange = (cladireId) => {
    setSelectedCladire(cladireId);
    fetchRoomsByCladire(cladireId);
  };

  return (
    <div className="professor-page">
      <header>
        <h1>
          Bun venit!
        </h1>
        <button className="logout-btn" onClick={onLogout}>
          Logout
        </button>
      </header>

      <section>
        <h2>Adaugă note pentru studenți</h2>
        <div>
          <label>Materie:</label>
          <select
  onChange={(e) => setSelectedMaterie(e.target.value)}
  value={selectedMaterie}
>
  <option value="">Selectați o materie</option>
  {materiiUnice.map((materie, index) => (
    <option key={index} value={materie}>
      {materie}
    </option>
  ))}
</select>

        </div>
        {students.map((student) => (
          <div key={student.cod}>
            <span>
              {student.nume} {student.prenume}
            </span>
            <input
              type="number"
              placeholder="Introduceți nota"
              onChange={(e) =>
                setStudentNotes({
                  ...studentNotes,
                  [student.cod]: e.target.value,
                })
              }
            />
          </div>
        ))}
        <button onClick={handleAddNotes}>Adaugă Note</button>
      </section>

      <div>
  <label>Tip:</label>
  <select
    onChange={(e) => setSelectedTip(e.target.value)}
    value={selectedTip}
  >
    <option value="">Selectați tipul</option>
    <option value="Curs">Curs</option>
    <option value="Laborator">Laborator</option>
    <option value="Seminar">Seminar</option>
  </select>
</div>

{/* Dacă profesorul selectează "Laborator", afișează opțiunea pentru semigrupă */}
{selectedTip === "Laborator" && (
  <div>
    <label>Semigrupa:</label>
    <input
      type="text"
      placeholder="Introduceți semigrupa (ex: 1 sau 2)"
      onChange={(e) => setSchedule({ ...schedule, semigrupa: e.target.value })}
    />
  </div>
)}


      <div>
        <label>Frecvența:</label>
        {(selectedTip === "Laborator" || selectedTip === "Seminar") && (
          <select
            onChange={(e) => setSelectedFrecventa(e.target.value)}
            value={selectedFrecventa}
          >
            <option value="">Selectați frecvența</option>
            <option value="1 sapt">1 săptămână</option>
            <option value="2 sapt">2 săptămâni</option>
            <option value="saptamanal">Săptămânal</option>
          </select>
        )}
      </div>

      <section>
        <h2>Rezervare sală</h2>
        <div>
          <label>Clădire:</label>
          <select
            onChange={(e) => handleCladireChange(e.target.value)}
            value={selectedCladire}
          >
            <option value="">Selectați clădirea</option>
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
            <option value="">Selectați sala</option>
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
            onChange={(e) => setSchedule({ ...schedule, zi: e.target.value })}
            value={schedule.zi}
          >
            <option value="">Selectați ziua</option>
            <option value="Luni">Luni</option>
            <option value="Marti">Marți</option>
            <option value="Miercuri">Miercuri</option>
            <option value="Joi">Joi</option>
            <option value="Vineri">Vineri</option>
          </select>
        </div>
        <button onClick={handleReserveRoom}>Rezervă Sală</button>
      </section>
    </div>
  );
}

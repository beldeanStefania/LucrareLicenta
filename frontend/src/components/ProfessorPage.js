import React, { useEffect, useState } from "react";
import { request } from "../helpers/axios-helper";
import "./ProfessorPage.css";

export default function ProfessorPage({ onLogout }) {
  const [professorInfo, setProfessorInfo] = useState(null); // Info despre profesor
  const [repartizari, setRepartizari] = useState([]); // Materii și activități
  const [students, setStudents] = useState([]); // Lista studenților
  const [selectedMaterie, setSelectedMaterie] = useState("");
  const [selectedTip, setSelectedTip] = useState("");
  const [studentNotes, setStudentNotes] = useState({}); // Note introduse pentru studenți
  const [cladiri, setCladiri] = useState([]); // Clădiri disponibile
  const [selectedCladire, setSelectedCladire] = useState(""); // Clădire selectată
  const [rooms, setRooms] = useState([]); // Săli disponibile
  const [selectedRoom, setSelectedRoom] = useState("");
  const [schedule, setSchedule] = useState({
    grupa: "",
    oraInceput: "",
    oraSfarsit: "",
    zi: "",
  });

  // Fetch user info, repartizari, and cladiri
  useEffect(() => {
    fetchProfessorInfo();
    fetchRepartizari();
    fetchCladiri();
  }, []);

  const fetchProfessorInfo = () => {
    request("GET", "/api/auth/userInfo")
      .then((response) => {
        setProfessorInfo(response.data);
      })
      .catch((error) => console.error("Failed to fetch professor info:", error));
  };

  const fetchRepartizari = () => {
    request("GET", "/api/repartizareProf")
      .then((response) => {
        setRepartizari(response.data);
      })
      .catch((error) => console.error("Failed to fetch repartizari:", error));
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
        .then(() => alert(`Nota ${nota} adăugată pentru student ${studentCod}`))
        .catch((error) => console.error("Failed to add grade:", error));
    });
  };

  const handleReserveRoom = () => {
    const repartizare = repartizari.find(
      (r) => r.materie === selectedMaterie && r.tip === selectedTip
    );

    if (!repartizare) {
      alert("Selectați o materie și un tip valid.");
      return;
    }

    request("POST", "/api/orare/add", {
      grupa: schedule.grupa,
      oraInceput: schedule.oraInceput,
      oraSfarsit: schedule.oraSfarsit,
      zi: schedule.zi,
      repartizareProfId: repartizare.id,
      salaId: selectedRoom,
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
        <h1>Welcome</h1>
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
            {repartizari.map((r) => (
              <option key={r.id} value={r.materie}>
                {r.materie} ({r.tip})
              </option>
            ))}
          </select>
        </div>
        {students.map((student) => (
          <div key={student.cod}>
            <span>{student.nume} {student.prenume}</span>
            <input
              type="number"
              placeholder="Introduceți nota"
              onChange={(e) =>
                setStudentNotes({ ...studentNotes, [student.cod]: e.target.value })
              }
            />
          </div>
        ))}
        <button onClick={handleAddNotes}>Adaugă Note</button>
      </section>

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
            onChange={(e) => setSchedule({ ...schedule, grupa: e.target.value })}
          />
        </div>
        <div>
          <label>Ora început:</label>
          <input
            type="time"
            onChange={(e) => setSchedule({ ...schedule, oraInceput: e.target.value })}
          />
        </div>
        <div>
          <label>Ora sfârșit:</label>
          <input
            type="time"
            onChange={(e) => setSchedule({ ...schedule, oraSfarsit: e.target.value })}
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

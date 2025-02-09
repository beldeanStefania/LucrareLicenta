import React, { useEffect, useState } from "react";
import { request } from "../helpers/axios-helper";
import "./ProfessorPage.css";

export default function ProfessorPage({ onLogout }) {
  // =================== State-uri generale ===================
  const [professorInfo, setProfessorInfo] = useState(null);

  // Materii/Repartizări asociate profesorului (de la /api/repartizareProf/materiiProfesor/{id})
  const [repartizari, setRepartizari] = useState([]);

  // =================== State-uri pentru clădiri/săli ===================
  const [cladiri, setCladiri] = useState([]);
  const [selectedCladire, setSelectedCladire] = useState("");
  const [rooms, setRooms] = useState([]);
  const [selectedRoom, setSelectedRoom] = useState("");

  // =================== State-uri pentru acordarea notelor ===================
  const [materiiUnice, setMateriiUnice] = useState([]); // Aceasta e lista (cod, denumire) folosită la notare
  const [selectedGradeMaterie, setSelectedGradeMaterie] = useState("");
  const [selectedGradeGroup, setSelectedGradeGroup] = useState("");
  const [gradeStudentNotes, setGradeStudentNotes] = useState({});
  const [students, setStudents] = useState([]);

  // =================== State-uri pentru rezervare sală (orar) ===================
  // Am separat materie pentru orar de materie pentru note
  const [selectedMaterieForSchedule, setSelectedMaterieForSchedule] = useState("");
  const [selectedTip, setSelectedTip] = useState("");
  const [selectedFrecventa, setSelectedFrecventa] = useState("");
  const [schedule, setSchedule] = useState({
    grupa: "",
    oraInceput: "",
    oraSfarsit: "",
    zi: "",
    semigrupa: "",
  });

  // =================== useEffect-uri ===================
  useEffect(() => {
    fetchProfessorInfo();
    fetchCladiri();
  }, []);

  useEffect(() => {
    if (professorInfo && professorInfo.profesorId) {
      fetchMateriiProfesor(professorInfo.profesorId);
    }
  }, [professorInfo]);

  // Când se schimbă grupa pentru notare, încărcăm studenții
  useEffect(() => {
    if (selectedGradeGroup) {
      fetchStudentsByGroup(selectedGradeGroup);
    }
  }, [selectedGradeGroup]);

  // =================== API Calls ===================
  const fetchProfessorInfo = () => {
    request("GET", "/api/auth/userInfo")
      .then((response) => {
        setProfessorInfo(response.data);
      })
      .catch((error) => console.error("Failed to fetch professor info:", error));
  };

  // Această funcție aduce "repartizări" (materii + tip) de la backend
  // Și de obicei conține ceva ca: {id, materie, tip, codMaterie, profesorId, ...}
  const fetchMateriiProfesor = (profesorId) => {
    request("GET", `/api/repartizareProf/materiiProfesor/${profesorId}`)
      .then((response) => {
        if (response.data && response.data.length > 0) {
          setRepartizari(response.data);
          // Pentru notare, dacă vrei doar un simplu "cod" și "denumire",
          // atunci extragi partea care te interesează:
          const materiiExtract = response.data.map((item) => ({
            cod: item.codMaterie,     // Ex. "MLR5000"
            denumire: item.materie,  // Ex. "Matematică logică"
          }));
          setMateriiUnice(materiiExtract);
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

  // =================== Handlers ===================
  // A. NOTARE STUDENȚI
  const handleAddGrades = () => {
    Object.entries(gradeStudentNotes).forEach(([studentCod, nota]) => {
      const payload = {
        studentCod,
        // Trimitem "selectedGradeMaterie". Observă că, în loc să fie un cod,
        // s-ar putea să fie un nume. Depinde cum e backend-ul tău setat.
        codMaterie: selectedGradeMaterie,
        nota: parseFloat(nota),
        semestru: 1,
      };
      console.log("payload trimis:", payload);

      request("POST", "/api/catalogStudentMaterie/add", payload)
        .then(() =>
          alert(`Nota ${nota} a fost adăugată pentru studentul ${studentCod}`)
        )
        .catch((error) => console.error("Failed to add grade:", error));
    });
  };

  // B. REZERVARE SALĂ (similar cu versiunea veche)
  const handleReserveRoom = () => {
    // Căutăm în `repartizari` obiectul care se potrivește cu materia + tipul selectate
    const repartizare = repartizari.find(
      (r) =>
        // r.materie poate fi exact denumirea. Depinde cum e structurat "repartizare" la tine
        r.materie === selectedMaterieForSchedule &&
        r.tip.toUpperCase() === selectedTip.toUpperCase()
    );

    const payload = {
      grupa: schedule.grupa,
      oraInceput: parseInt(schedule.oraInceput.split(":")[0], 10),
      oraSfarsit: parseInt(schedule.oraSfarsit.split(":")[0], 10),
      zi: schedule.zi,
      repartizareProfId: repartizare?.id || null,
      profesorId: professorInfo.profesorId,
      materie: selectedMaterieForSchedule, // atenție: poate fi nume materie sau cod
      tip: selectedTip,
      salaId: selectedRoom,
      frecventa:
        selectedTip === "Laborator" || selectedTip === "Seminar"
          ? selectedFrecventa
          : "",
      semigrupa: selectedTip === "Laborator" ? schedule.semigrupa : null,
    };

    console.log("Rezervare sala payload:", payload);

    request("POST", "/api/orare/add", payload)
      .then(() => alert("Sală rezervată cu succes!"))
      .catch((error) => console.error("Failed to reserve room:", error));
  };

  // C. Când selectăm o clădire, aducem sălile
  const handleCladireChange = (cladireId) => {
    setSelectedCladire(cladireId);
    fetchRoomsByCladire(cladireId);
  };

  // =================== RENDER ===================
  return (
    <div className="professor-page">
      <header>
        <h1>Bun venit!</h1>
        <button className="logout-btn" onClick={onLogout}>
          Logout
        </button>
      </header>

      {/* --------------------------------------------------
          1) Acordă note studenților 
      -------------------------------------------------- */}
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
            placeholder="Introdu grupa"
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

      {/* --------------------------------------------------
          2) Rezervare sală (Orar)
      -------------------------------------------------- */}
      <section>
        <h2>Rezervare sală</h2>

        {/* Materie pentru orar (diferită de materie pentru note) */}
        <div>
          <label>Materie pentru orar:</label>
          <select
            onChange={(e) => setSelectedMaterieForSchedule(e.target.value)}
            value={selectedMaterieForSchedule}
          >
            <option value="">Selectează materie</option>
            {repartizari.map((item, index) => (
              <option key={index} value={item.materie}>
                {item.materie}
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

        {/* Frecvența apare doar pentru Laborator/Seminar */}
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

        {/* Semigrupa apare doar pentru Laborator */}
        {selectedTip === "Laborator" && (
          <div>
            <label>Semigrupa:</label>
            <input
              type="text"
              placeholder="ex: 1 sau 2"
              onChange={(e) =>
                setSchedule({ ...schedule, semigrupa: e.target.value })
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
            onChange={(e) => setSchedule({ ...schedule, grupa: e.target.value })}
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
            <option value="">Selectează ziua</option>
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

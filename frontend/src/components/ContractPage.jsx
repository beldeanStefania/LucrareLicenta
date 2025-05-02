import React, { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";
import axios from "axios";
import NavigationHeader from "./NavigationHeader";
import "./StudentPage.css";

export default function ContractSelectionPage({ onLogout }) {
  const [searchParams] = useSearchParams();
  const cod = searchParams.get("cod");
  const semestru = searchParams.get("semestru");

  const [requiredCourses, setRequiredCourses] = useState([]);
  const [availableCourses, setAvailableCourses] = useState([]);
  const [selectedCourses, setSelectedCourses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [generating, setGenerating] = useState(false);
  const [previewing, setPreviewing] = useState(false);

  // Încarcă materiile obligatorii
  useEffect(() => {
    if (!cod || !semestru) return;
    axios
      .get(`http://localhost:8080/api/curriculum/materii-obligatorii/${cod}/${semestru}`)
      .then(res => setRequiredCourses(res.data))
      .catch(err => {
        console.error(err);
        alert("Nu s-au putut încărca materiile obligatorii.");
      });
  }, [cod, semestru]);

  // Încarcă materiile disponibile pentru alegere (opționale/facultative)
  useEffect(() => {
    if (!cod || !semestru) return;
    setLoading(true);
    axios
      .get(`http://localhost:8080/api/studentContract/availableCourses/${cod}/${semestru}`)
      .then(res => setAvailableCourses(res.data))
      .catch(err => {
        console.error(err);
        alert("Nu s-au putut încărca materiile disponibile.");
      })
      .finally(() => setLoading(false));
  }, [cod, semestru]);

  const toggleCourse = (codMaterie) => {
    setSelectedCourses(prev =>
      prev.includes(codMaterie)
        ? prev.filter(c => c !== codMaterie)
        : [...prev, codMaterie]
    );
  };

  const generateContract = async () => {
    const allCourses = [
      ...requiredCourses.map(m => m.cod),
      ...selectedCourses
    ];

    if (allCourses.length === 0) {
      alert("Selectează cel puțin o materie pentru contract.");
      return;
    }

    setGenerating(true);
    try {
      const res = await axios.post(
        "http://localhost:8080/api/studentContract/generateContract",
        {
          studentCod: cod,
          semestru: parseInt(semestru, 10),
          coduriMaterii: allCourses
        },
        { responseType: "blob" }
      );
      const blob = new Blob([res.data], { type: "application/pdf" });
      const url  = URL.createObjectURL(blob);
      const a    = document.createElement("a");
      a.href     = url;
      a.download = `contract_${cod}.pdf`;
      document.body.appendChild(a);
      a.click();
      a.remove();
      setTimeout(() => URL.revokeObjectURL(url), 5000);
    } catch (err) {
      console.error(err);
      alert("A apărut o eroare la generarea contractului.");
    } finally {
      setGenerating(false);
    }
  };

  const previewContract = async () => {
    const allCourses = [
      ...requiredCourses.map(m => m.cod),
      ...selectedCourses
    ];

    if (allCourses.length === 0) {
      alert("Selectează cel puțin o materie pentru previzualizare.");
      return;
    }

    setPreviewing(true);
    try {
      const params = new URLSearchParams();
      params.append("studentCod", cod);
      params.append("semestru", semestru);
      allCourses.forEach(c => params.append("coduriMaterii", c));

      const res = await axios.get(
        "http://localhost:8080/api/studentContract/preview",
        { params, responseType: "blob" }
      );
      const blob = new Blob([res.data], { type: "application/pdf" });
      const url  = URL.createObjectURL(blob);
      window.open(url, "_blank");
      setTimeout(() => URL.revokeObjectURL(url), 5000);
    } catch (err) {
      console.error(err);
      alert("A apărut o eroare la previzualizare.");
    } finally {
      setPreviewing(false);
    }
  };

  return (
    <div className="student-page">
      <NavigationHeader userRole="ROLE_STUDENT" onLogout={onLogout} />

      <div className="student-content">
        <h1 className="welcome-title">Selectează materiile pentru contract</h1>
        <p className="welcome-subtitle">Semestrul {semestru}</p>

        {loading ? (
          <div className="loading-container">
            <div className="loading-spinner" />
            <div className="loading-text">Se încarcă materiile...</div>
          </div>
        ) : (
          <>
            {requiredCourses.length > 0 && (
              <div className="table-wrapper">
                <h3>Materii Obligatorii</h3>
                <table className="data-table">
                  <thead>
                    <tr>
                      <th>Nume Materie</th>
                      <th>Cod</th>
                      <th>Credite</th>
                      <th>Semestru</th>
                    </tr>
                  </thead>
                  <tbody>
                    {requiredCourses.map(m => (
                      <tr key={m.cod}>
                        <td>{m.nume}</td>
                        <td>{m.cod}</td>
                        <td>{m.credite}</td>
                        <td>{m.semestru}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}

            <div className="table-wrapper">
              <h3>Materii Opționale / Facultative</h3>
              <table className="data-table">
                <thead>
                  <tr>
                    <th></th>
                    <th>Nume Materie</th>
                    <th>Cod</th>
                    <th>Credite</th>
                  </tr>
                </thead>
                <tbody>
                  {availableCourses.map(m => (
                    <tr key={m.cod}>
                      <td>
                        <input
                          type="checkbox"
                          checked={selectedCourses.includes(m.cod)}
                          onChange={() => toggleCourse(m.cod)}
                        />
                      </td>
                      <td>{m.nume}</td>
                      <td>{m.cod}</td>
                      <td>{m.credite}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>

            <div className="actions">
              <button
                onClick={generateContract}
                disabled={generating}
                className="btn-generate"
              >
                {generating ? "Generează..." : "Generează Contractul"}
              </button>
              <button
                onClick={previewContract}
                disabled={previewing}
                className="btn-preview"
              >
                {previewing
                  ? "Încarcă previzualizare..."
                  : "Previzualizează Contractul"}
              </button>
            </div>
          </>
        )}
      </div>
    </div>
  );
}

// src/components/ContractSelectionPage.jsx
import React, { useEffect, useState } from "react";
import { request } from "../helpers/axios-helper"; 
import { useSearchParams } from "react-router-dom";
import axios from "axios";
import NavigationHeader from "./NavigationHeader";
import "./StudentPage.css";

export default function ContractSelectionPage({ onLogout }) {
  const [searchParams] = useSearchParams();

  const [cod, setCod] = useState("");
  const [an, setAn] = useState(null);
  const [selectedSet, setSelectedSet] = useState(new Set());
  const [filter, setFilter] = useState("");
  const [loading, setLoading] = useState(false);
  const [generating, setGenerating] = useState(false);
  const [previewing, setPreviewing] = useState(false);
  const [suggestions, setSuggestions] = useState([]);
  const [exists, setExists] = useState(null);
  const [sem1Courses, setSem1Courses] = useState([]);
  const [sem2Courses, setSem2Courses] = useState([]);



  useEffect(() => {
    if (filter.length < 2) {
      setSuggestions([]);
      return;
    }
    request("GET", `/api/materie/search?q=${encodeURIComponent(filter)}`)
      .then(res => setSuggestions(res.data || []))
      .catch(() => setSuggestions([]));
  }, [filter]);
  
  

  // 2) Fetch cursuri
  useEffect(() => {
    if (!cod || an == null) return;
    setLoading(true);
  
    Promise.all([
      request("GET", `/api/studentContract/availableCourses/${cod}/${an}/1`),
      request("GET", `/api/studentContract/availableCourses/${cod}/${an}/2`)
    ])
    .then(([res1, res2]) => {
      setSem1Courses(res1.data || []);
      setSem2Courses(res2.data || []);
      // bifează automat ce era deja `selected` în DTO‐uri
      const pre = new Set([
        ...res1.data.filter(c => c.selected).map(c => c.cod),
        ...res2.data.filter(c => c.selected).map(c => c.cod)
      ]);
      setSelectedSet(pre);
    })
    .catch(err => {
      console.error(err);
      alert("Nu s-au putut încărca cursurile disponibile.");
    })
    .finally(() => setLoading(false));
  }, [cod, an]);
  

  // 3) Toggle selectare
  const toggle = codMaterie => {
    const in1 = sem1Courses.find(c => c.cod === codMaterie);
    const in2 = sem2Courses.find(c => c.cod === codMaterie);
    const course = in1 || in2;
    if (!course || course.tip === "OBLIGATORIE") return;
    const ns = new Set(selectedSet);
    ns.has(codMaterie) ? ns.delete(codMaterie) : ns.add(codMaterie);
    setSelectedSet(ns);
  };

  const handleSubmit = async (preview) => {
    if (selectedSet.size === 0) {
      alert("Trebuie să ai cel puțin o materie selectată.");
      return;
    }
    const payload = {
      studentCod: cod,
      anContract: an,
      coduriMaterii: Array.from(selectedSet),
    };
  
    try {
      preview ? setPreviewing(true) : setGenerating(true);
  
      const endpoint = preview
        ? "/api/studentContract/preview"
        : "/api/studentContract/generateContract";
  
      // apel cu helper-ul request, care adaugă și token-ul
      const response = await request(
        "POST",
        endpoint,
        payload,
        { responseType: "blob" }
      );
  
      // dacă a venit PDF-ul
      const blob = new Blob([response.data], { type: "application/pdf" });
      const url = URL.createObjectURL(blob);
      if (preview) {
        window.open(url, "_blank");
      } else {
        const a = document.createElement("a");
        a.href = url;
        a.download = `contract_${cod}.pdf`;
        document.body.appendChild(a);
        a.click();
        a.remove();
      }
      setTimeout(() => URL.revokeObjectURL(url), 5000);
  
    } catch (err) {
      // Ai un err.response.data de tip Blob chiar și pe 403/400:
      let message = "Eroare la generarea contractului.";
      if (err.response?.data instanceof Blob) {
        // citește blob-ul ca text
        message = await err.response.data.text();
      } else if (err.response?.data) {
        // poate fi JSON
        const d = err.response.data;
        message = d.message || JSON.stringify(d);
      }
      alert(message);
    } finally {
      preview ? setPreviewing(false) : setGenerating(false);
    }
  };
  

  
  // 1) Extragem cod + an și verificăm existența contractului
useEffect(() => {
  const studentCod = searchParams.get("cod");
  const studentAn  = parseInt(searchParams.get("an"), 10);
  if (!studentCod || isNaN(studentAn)) return;

  setCod(studentCod);

  // Mai întâi, vedem dacă există contract pentru anul original
  request("GET", `/api/studentContract/exists/${studentCod}/${studentAn}`)
    .then(res => {
      if (res.data === true) {
        // dacă da, mergem pe anul următor
        setAn(studentAn + 1);
      } else {
        // altfel, rămânem pe anul curent
        setAn(studentAn);
      }
    })
    .catch(err => {
      console.error("Eroare la exists:", err);
      // în caz de eroare, lăsăm an neschimbat
      setAn(studentAn);
    });
}, [searchParams]);

  return (
    <div className="student-page">
      <NavigationHeader userRole="ROLE_STUDENT" onLogout={onLogout} />

      <div className="student-content">
        <h1 className="welcome-title">
          Selectează materiile pentru anul {an ?? "..."}
        </h1>

        {/* SEARCH + AUTOCOMPLETE */}
        <form
          onSubmit={e => e.preventDefault()}
          style={{ position: "relative" }}
        >
          <input
            type="text"
            placeholder="Caută materie..."
            value={filter}
            onChange={e => setFilter(e.target.value)}
            onKeyDown={e => { if (e.key === "Enter") e.preventDefault(); }}
            style={{
              margin: "16px 0",
              padding: "8px",
              width: "100%",
              boxSizing: "border-box"
            }}
          />

          {suggestions.length > 0 && (
            <ul className="suggestions-list">
              {suggestions.map(m => (
                <li
                  key={m.cod}
                  onClick={e => {
                    e.stopPropagation();
                    // 1) marchează selectat
                    setSelectedSet(prev => {
                      const next = new Set(prev);
                      next.add(m.cod);
                      return next;
                    });
                    // 2) adaugă în courses
                    if (m.semestru === 1) {
                      setSem1Courses(prev =>
                          prev.some(c => c.cod === m.cod)
                            ? prev
                            : [...prev, { ...m, tip: "OPTIONALA", selected: true }]
                      );
                    } else {
                      setSem2Courses(prev =>
                        prev.some(c => c.cod === m.cod)
                          ? prev
                           : [...prev, { ...m, tip: "OPTIONALA", selected: true }]
                     );
                    }
                    // 3) curăță
                    setSuggestions([]);
                    setFilter("");
                  }}
                >
                  {m.nume} ({m.cod})
                </li>
              ))}
            </ul>
          )}
        </form>

        {/* TABEL CURSURI */}
        {loading ? (
          <div className="loading-container">
            <div className="loading-spinner" />
            <div className="loading-text">Se încarcă cursurile...</div>
          </div>
        ) : (
          <>
            <h2>Semestrul I</h2>
            <table className="data-table">
              <thead>
                <tr>
                  <th></th><th>Nume</th><th>Cod</th>
                  <th>Credite</th><th>Tip</th>
                </tr>
              </thead>
              <tbody>
                {sem1Courses
                  .filter(c =>
                    c.nume.toLowerCase().includes(filter.toLowerCase()) ||
                    c.cod.toLowerCase().includes(filter.toLowerCase())
                  )
                  .map(c => (
                    <tr key={c.cod}>
                      <td>
                        <input
                          type="checkbox"
                          checked={selectedSet.has(c.cod)}
                          disabled={c.tip === "OBLIGATORIE"}
                          onChange={() => toggle(c.cod)}
                        />
                      </td>
                      <td>{c.nume}</td>
                      <td>{c.cod}</td>
                      <td>{c.credite}</td>
                      <td>{c.tip}</td>
                    </tr>
                  ))}
              </tbody>
            </table>

            <h2>Semestrul II</h2>
            <table className="data-table">
              <thead>
                <tr>
                  <th></th><th>Nume</th><th>Cod</th>
                  <th>Credite</th><th>Tip</th>
                </tr>
              </thead>
              <tbody>
                {sem2Courses
                  .filter(c =>
                    c.nume.toLowerCase().includes(filter.toLowerCase()) ||
                    c.cod.toLowerCase().includes(filter.toLowerCase())
                  )
                  .map(c => (
                    <tr key={c.cod}>
                      <td>
                        <input
                          type="checkbox"
                          checked={selectedSet.has(c.cod)}
                          disabled={c.tip === "OBLIGATORIE"}
                          onChange={() => toggle(c.cod)}
                        />
                      </td>
                      <td>{c.nume}</td>
                      <td>{c.cod}</td>
                      <td>{c.credite}</td>
                      <td>{c.tip}</td>
                    </tr>
                  ))}
              </tbody>
            </table>

            <div className="actions">
              <button onClick={() => handleSubmit(false)} disabled={generating}>
                {generating ? "Generează..." : "Generează PDF"}
              </button>
              <button onClick={() => handleSubmit(true)} disabled={previewing}>
                {previewing ? "Încarcă previzualizare..." : "Previzualizează"}
              </button>
            </div>
          </>
        )}
      </div>
    </div>
  );
}
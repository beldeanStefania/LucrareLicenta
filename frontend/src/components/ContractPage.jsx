import React, { useEffect, useState } from "react";
import axios from "axios";
import { useSearchParams } from "react-router-dom";
import NavigationHeader from "./NavigationHeader";
import "./StudentPage.css";

// Folosește import.meta.env pentru Vite sau default la localhost
const API_BASE = import.meta.env.VITE_API_URL || "http://localhost:8080";

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
  const [sem1Courses, setSem1Courses] = useState([]);
  const [sem2Courses, setSem2Courses] = useState([]);

  // Autocomplete
  useEffect(() => {
    if (filter.length < 2) {
      setSuggestions([]);
      return;
    }
    axios.get(`${API_BASE}/api/materie/search?q=${encodeURIComponent(filter)}`)
      .then(res => setSuggestions(res.data || []))
      .catch(() => setSuggestions([]));
  }, [filter]);

  // Verificăm existența contractului
  useEffect(() => {
    const studentCod = searchParams.get("cod");
    const studentAn  = parseInt(searchParams.get("an"), 10);
    if (!studentCod || isNaN(studentAn)) return;
    setCod(studentCod);
    axios.get(`${API_BASE}/api/studentContract/exists/${studentCod}/${studentAn}`)
      .then(res => setAn(res.data ? studentAn + 1 : studentAn))
      .catch(() => setAn(studentAn));
  }, [searchParams]);

  // Fetch cursuri și preselect obligatorii
  useEffect(() => {
    if (!cod || an == null) return;
    setLoading(true);
    Promise.all([
      axios.get(`${API_BASE}/api/studentContract/availableCourses/${cod}/${an}/1`),
      axios.get(`${API_BASE}/api/studentContract/availableCourses/${cod}/${an}/2`)
    ])
    .then(([res1, res2]) => {
      const list1 = res1.data || [];
      const list2 = res2.data || [];
      setSem1Courses(list1);
      setSem2Courses(list2);
      const pre = new Set([
        ...list1.filter(c => c.tip === "OBLIGATORIE").map(c => c.cod),
        ...list2.filter(c => c.tip === "OBLIGATORIE").map(c => c.cod),
        ...list1.filter(c => c.selected).map(c => c.cod),
        ...list2.filter(c => c.selected).map(c => c.cod)
      ]);
      setSelectedSet(pre);
    })
    .catch(err => {
      console.error(err);
      alert("Nu s-au putut încărca cursurile disponibile.");
    })
    .finally(() => setLoading(false));
  }, [cod, an]);

  // Toggle selectare
  const toggle = codMaterie => {
    const course = sem1Courses.concat(sem2Courses).find(c => c.cod === codMaterie);
    if (!course || course.tip === "OBLIGATORIE") return;
    const ns = new Set(selectedSet);
    ns.has(codMaterie) ? ns.delete(codMaterie) : ns.add(codMaterie);
    setSelectedSet(ns);
  };

  // Generare / previzualizare PDF prin axios
  const handleSubmit = async (preview) => {
    if (selectedSet.size === 0) {
      alert("Trebuie să ai cel puțin o materie selectată.");
      return;
    }

    const payload = {
      studentCod: cod,
      anContract: an,
      coduriMaterii: Array.from(selectedSet)
    };

    try {
      preview ? setPreviewing(true) : setGenerating(true);
      const url = `${API_BASE}/api/studentContract/${preview ? 'preview' : 'generateContract'}`;
      const response = await axios.post(url, payload, {
        responseType: 'blob',
        headers: { 'Content-Type': 'application/json' }
      });

      const blob = new Blob([response.data], { type: 'application/pdf' });
      const blobUrl = URL.createObjectURL(blob);

      if (preview) {
        window.open(blobUrl, '_blank');
      } else {
        const a = document.createElement('a');
        a.href = blobUrl;
        a.download = `contract_${cod}.pdf`;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
      }

      setTimeout(() => URL.revokeObjectURL(blobUrl), 5000);
    } catch (err) {
      console.error(err);
      let message = 'Eroare la generarea contractului.';
      if (err.response && err.response.data) {
        try { message = await err.response.data.text(); } catch {};
      }
      alert(message);
    } finally {
      preview ? setPreviewing(false) : setGenerating(false);
    }
  };

  return (
    <div className="student-page">
      <NavigationHeader userRole="ROLE_STUDENT" onLogout={onLogout} />

      <div className="student-content">
        <h1 className="welcome-title">
          Selectează materiile pentru anul {an ?? '...'}
        </h1>

        <form onSubmit={e => e.preventDefault()} style={{ position: 'relative' }}>
          <input
            type="text"
            placeholder="Caută materie..."
            value={filter}
            onChange={e => setFilter(e.target.value)}
            style={{ margin: '16px 0', padding: '8px', width: '100%', boxSizing: 'border-box' }}
          />
          {suggestions.length > 0 && (
            <ul className="suggestions-list">
              {suggestions.map(m => (
                <li key={m.cod} onClick={() => {
                  setSelectedSet(prev => new Set([...prev, m.cod]));
                  const list = m.semestru === 1 ? sem1Courses : sem2Courses;
                  const setter = m.semestru === 1 ? setSem1Courses : setSem2Courses;
                  setter(prev => prev.some(c => c.cod === m.cod)
                    ? prev
                    : [...prev, { ...m, tip: 'OPTIONALA', selected: true }]
                  );
                  setSuggestions([]);
                  setFilter('');
                }}>{m.nume} ({m.cod})</li>
              ))}
            </ul>
          )}

        </form>

        {loading ? (
          <div className="loading-container">
            <div className="loading-spinner" />
            <div className="loading-text">Se încarcă cursurile...</div>
          </div>
        ) : (
          <>
            <h2>Semestrul I</h2>
            <table className="data-table">
              <thead><tr><th></th><th>Nume</th><th>Cod</th><th>Credite</th><th>Tip</th></tr></thead>
              <tbody>
                {sem1Courses.filter(c => c.nume.toLowerCase().includes(filter.toLowerCase()) || c.cod.toLowerCase().includes(filter.toLowerCase())).map(c => (
                  <tr key={c.cod}>
                    <td><input type="checkbox" checked={selectedSet.has(c.cod)} disabled={c.tip === 'OBLIGATORIE'} onChange={() => toggle(c.cod)} /></td>
                    <td>{c.nume}</td><td>{c.cod}</td><td>{c.credite}</td><td>{c.tip}</td>
                  </tr>
                ))}
              </tbody>
            </table>

            <h2>Semestrul II</h2>
            <table className="data-table">
              <thead><tr><th></th><th>Nume</th><th>Cod</th><th>Credite</th><th>Tip</th></tr></thead>
              <tbody>
                {sem2Courses.filter(c => c.nume.toLowerCase().includes(filter.toLowerCase()) || c.cod.toLowerCase().includes(filter.toLowerCase())).map(c => (
                  <tr key={c.cod}>
                    <td><input type="checkbox" checked={selectedSet.has(c.cod)} disabled={c.tip === 'OBLIGATORIE'} onChange={() => toggle(c.cod)} /></td>
                    <td>{c.nume}</td><td>{c.cod}</td><td>{c.credite}</td><td>{c.tip}</td>
                  </tr>
                ))}
              </tbody>
            </table>

            <div className="actions">
              <button onClick={() => handleSubmit(false)} disabled={generating}>{generating ? 'Generează...' : 'Generează PDF'}</button>
              <button onClick={() => handleSubmit(true)} disabled={previewing}>{previewing ? 'Încarcă previzualizare...' : 'Previzualizează'}</button>
            </div>
          </>
        )}
      </div>
    </div>
  );
}

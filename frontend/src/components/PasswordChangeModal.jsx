import React, { useState } from "react";
import { request } from "../helpers/axios-helper";
import "./PasswordChangeModal.css";

export default function PasswordChangeModal({ username, onClose }) {
  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [errorMsg, setErrorMsg] = useState("");
  const [successMsg, setSuccessMsg] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrorMsg("");
    setSuccessMsg("");

    if (!oldPassword || !newPassword || !confirmPassword) {
      setErrorMsg("Toate câmpurile sunt obligatorii.");
      return;
    }
    if (newPassword !== confirmPassword) {
      setErrorMsg("Parolele noi nu coincid.");
      return;
    }
    if (newPassword.length < 6) {
      setErrorMsg("Parola nouă trebuie să aibă cel puțin 6 caractere.");
      return;
    }

    setIsSubmitting(true);
    try {
      const payload = {
        username,
        oldPassword,
        newPassword
      };
      const response = await request("POST", "/api/auth/changePassword", payload);
      setSuccessMsg("Parola a fost schimbată cu succes.");
      setTimeout(() => {
        onClose();
      }, 1500);
    } catch (err) {
      console.error("Eroare la schimbarea parolei:", err);
      const msg = err.response?.data || "A apărut o eroare. Încearcă din nou.";
      setErrorMsg(msg);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="pwd-modal-overlay" onClick={onClose}>
      <div className="pwd-modal-container" onClick={(e) => e.stopPropagation()}>
        <h2>Schimbă parola</h2>
        <form onSubmit={handleSubmit} className="pwd-modal-form">
          {errorMsg && <div className="pwd-error">{errorMsg}</div>}
          {successMsg && <div className="pwd-success">{successMsg}</div>}

          <label htmlFor="oldPassword">Parola curentă</label>
          <input
            id="oldPassword"
            type="password"
            value={oldPassword}
            onChange={(e) => setOldPassword(e.target.value)}
            placeholder="Introdu parola curentă"
            disabled={isSubmitting || successMsg}
          />

          <label htmlFor="newPassword">Parola nouă</label>
          <input
            id="newPassword"
            type="password"
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
            placeholder="Introdu parola nouă"
            disabled={isSubmitting || successMsg}
          />

          <label htmlFor="confirmPassword">Confirmă parola nouă</label>
          <input
            id="confirmPassword"
            type="password"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
            placeholder="Reintrodu parola nouă"
            disabled={isSubmitting || successMsg}
          />

          <div className="pwd-buttons">
            <button
              type="submit"
              className="btn-submit"
              disabled={isSubmitting || !!successMsg}
            >
              {isSubmitting ? "Se procesează..." : "Salvează"}
            </button>
            <button
              type="button"
              className="btn-cancel"
              onClick={onClose}
              disabled={isSubmitting}
            >
              Anulează
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

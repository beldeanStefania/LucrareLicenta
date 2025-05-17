import React, { useState, useEffect, useRef } from 'react';
import { FaComments, FaTimes } from 'react-icons/fa';
import { request } from '../helpers/axios-helper';
import './ChatWidget.css';

export default function ChatWidget() {
  const [open, setOpen] = useState(false);
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState('');
  const [loading, setLoading] = useState(false);
  const endRef = useRef(null);

  const toggleOpen = () => setOpen(o => !o);

  const scrollToBottom = () => {
    endRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  useEffect(scrollToBottom, [messages, open]);

  const sendMessage = async () => {
    if (!input.trim()) return;
    const content = input.trim();
    setMessages(msgs => [...msgs, { sender: 'user', content }]);
    setInput('');
    setLoading(true);

    try {
      const res = await request('POST', '/api/chat', { message: content });
      setMessages(msgs => [...msgs, { sender: 'bot', content: res.reply }]);
    } catch {
      setMessages(msgs => [...msgs, { sender: 'bot', content: 'A apărut o eroare. Încearcă din nou.' }]);
    } finally {
      setLoading(false);
    }
  };

  const onKeyDown = e => {
    if (e.key === 'Enter') sendMessage();
  };

  return (
    <>
      <div className="chat-toggle-btn" onClick={toggleOpen}>
        {open ? <FaTimes size={20}/> : <FaComments size={20}/>}
      </div>

      {open && (
        <div className="chat-window">
          <div className="chat-header">Asistent Facultate</div>
          <div className="chat-messages">
            {messages.map((m,i) => (
              <div key={i} className={`chat-message ${m.sender}`}>
                {m.content}
              </div>
            ))}
            <div ref={endRef}/>
          </div>
          <div className="chat-input-area">
            <input
              type="text"
              placeholder="Scrie un mesaj..."
              value={input}
              onChange={e => setInput(e.target.value)}
              onKeyDown={onKeyDown}
              disabled={loading}
            />
            <button onClick={sendMessage} disabled={loading}>
              {loading ? '…' : 'Trimite'}
            </button>
          </div>
        </div>
      )}
    </>
  );
}

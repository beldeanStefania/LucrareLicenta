import React, { useState, useRef, useEffect } from 'react';
import { FaComments, FaTimes } from 'react-icons/fa';
import ReactMarkdown from 'react-markdown';
import { request } from '../helpers/axios-helper';
import './ChatWidget.css';

export default function ChatWidget() {
  const [open, setOpen] = useState(false);
  const [messages, setMessages] = useState([
    { from: 'bot', text: 'Salut! Cu ce te pot ajuta azi?' }
  ]);
  const [input, setInput] = useState('');
  const endRef = useRef(null);

  const role = localStorage.getItem('role') || 'student';

  // scroll la fiecare mesaj nou
  useEffect(() => {
    endRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages]);

  const send = async () => {
    if (!input.trim()) return;

    const userMsg = { from: 'user', text: input };
    setMessages(msgs => [...msgs, userMsg]);
    setInput('');

    try {
      const res = await request('POST', '/api/chat', { message: input });
      console.log('GPT response raw:', res);
      const botReply = res.data?.reply ?? 'Îmi pare rău, nu am răspuns.';
      setMessages(msgs => [...msgs, { from: 'bot', text: botReply }]);
    } catch (e) {
      console.error('Chat error', e);
    setMessages(msgs => [
      ...msgs,
      { from: 'bot', text: 'A apărut o eroare de server.' }
    ]);
    }
  };

  return (
    <>
      {open && (
        <div className="cw-container">
          <div className="cw-header">
            <span>Asistent Virtual</span>
            <FaTimes onClick={() => setOpen(false)} className="cw-close"/>
          </div>
          <div className="cw-body">
            {messages.map((m,i) => (
              <div key={i} className={`cw-message ${m.from}`}>
                <ReactMarkdown>{m.text}</ReactMarkdown>
              </div>
            ))}
            <div ref={endRef} />
          </div>
          <div className="cw-footer">
            <input
              value={input}
              onChange={e => setInput(e.target.value)}
              onKeyDown={e => e.key === 'Enter' && send()}
              placeholder="Scrie mesaj..."
            />
            <button onClick={send}>Trimite</button>
          </div>
        </div>
      )}
      <div className="cw-toggle" onClick={() => setOpen(o => !o)}>
        <FaComments size={24}/>
      </div>
    </>
  );
}

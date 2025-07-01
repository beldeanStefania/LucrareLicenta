import React, { useEffect, useState } from "react";
import { request } from "../helpers/axios-helper";

export default function TodoSection({ username }) {
  const [todos, setTodos] = useState([]);
  const [newTitle, setNewTitle] = useState("");
  const [newDescription, setNewDescription] = useState("");
  const [newDeadline, setNewDeadline] = useState("");
  const [loadingTodos, setLoadingTodos] = useState(true);

  const fetchTodos = () => {
    setLoadingTodos(true);
    request("GET", `/api/todo/user/${username}`)
      .then(res => setTodos(res.data || []))
      .catch(() => setTodos([]))
      .finally(() => setLoadingTodos(false));
  };

  useEffect(() => {
    if (username) fetchTodos();
  }, [username]);

  const handleAddTodo = () => {
    if (!newTitle || !newDeadline) return;
    request("POST", "/api/todo/create", {
      username,
      title: newTitle,
      description: newDescription,
      deadline: newDeadline
    })
      .then(() => {
        setNewTitle(""); setNewDescription(""); setNewDeadline("");
        fetchTodos();
      })
      .catch(() => alert("Eroare la crearea To-Do-ului!"));
  };

  const handleMarkDone = id =>
    request("PUT", `/api/todo/update/${id}`, { ...todos.find(t=>t.id===id), done: true })
      .then(fetchTodos);

  const handleDeleteTodo = id =>
    request("DELETE", `/api/todo/delete/${id}`)
      .then(fetchTodos);

  return (
    <div id="todo" className="section-container">
      <div className="section-header"><h2>To-Do List</h2></div>
      <div className="section-content">
        <div className="todo-form">
          <input
            type="text" placeholder="Titlu"
            value={newTitle} onChange={e=>setNewTitle(e.target.value)}
          />
          <input
            type="text" placeholder="Descriere (opțional)"
            value={newDescription}
            onChange={e=>setNewDescription(e.target.value)}
          />
          <input
            type="date" value={newDeadline}
            onChange={e=>setNewDeadline(e.target.value)}
          />
          <button onClick={handleAddTodo}>Adaugă</button>
        </div>

        {loadingTodos
          ? (
            <div className="loading-container">
              <div className="loading-spinner"/>
              <p>Se încarcă To-Do-urile...</p>
            </div>
          )
          : Array.isArray(todos) && todos.length > 0
            ? (
              <table className="data-table">
                <thead>
                  <tr>
                    <th>Titlu</th><th>Descriere</th><th>Deadline</th>
                    <th>Status</th><th>Acțiuni</th>
                  </tr>
                </thead>
                <tbody>
                  {todos.map(todo=>(
                    <tr key={todo.id}>
                      <td>{todo.title}</td>
                      <td>{todo.description||"—"}</td>
                      <td>{todo.deadline}</td>
                      <td>{todo.done?"Finalizat":"Nefinalizat"}</td>
                      <td>
                        {!todo.done && (
                          <button onClick={()=>handleMarkDone(todo.id)}>
                            Marchează ca done
                          </button>
                        )}
                        <button onClick={()=>handleDeleteTodo(todo.id)}>
                          Șterge
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )
            : <p>Nu există niciun To-Do momentan.</p>
        }
      </div>
    </div>
  );
}

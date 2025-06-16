import { useState, useEffect } from "react";

function TodoList({ username, onLogout }) {
  const [todos, setTodos] = useState([]);
  const [newTodo, setNewTodo] = useState("");

  const token = localStorage.getItem("token");

  useEffect(() => {
    fetch("http://localhost:8080/api/todos", {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then((res) => res.json())
      .then((data) => setTodos(data))
      .catch((err) => console.error("Todo çekilemedi", err));
  }, []);

  const addTodo = () => {
    if (!newTodo.trim()) return;
    fetch("http://localhost:8080/api/todos", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({ title: newTodo, completed: false }),
    })
      .then((res) => res.json())
      .then((createdTodo) => {
        setTodos([...todos, createdTodo]);
        setNewTodo("");
      });
  };

  const toggleComplete = (todo) => {
    fetch(`http://localhost:8080/api/todos/${todo.id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({ ...todo, completed: !todo.completed }),
    })
      .then((res) => res.json())
      .then((updatedTodo) => {
        setTodos(
          todos.map((t) => (t.id === updatedTodo.id ? updatedTodo : t))
        );
      });
  };

  const deleteTodo = (id) => {
    fetch(`http://localhost:8080/api/todos/${id}`, {
      method: "DELETE",
      headers: { Authorization: `Bearer ${token}` },
    }).then(() => {
      setTodos(todos.filter((t) => t.id !== id));
    });
  };

  return (
    <div>
      <h2>Todo Listesi</h2>
      <p>👋 Hoş geldin, <strong>{username}</strong>!</p>
      <button onClick={onLogout}>Çıkış Yap</button>

      <div style={{ marginTop: "20px" }}>
        <input
          type="text"
          placeholder="Yeni todo"
          value={newTodo}
          onChange={(e) => setNewTodo(e.target.value)}
        />
        <button onClick={addTodo}>Ekle</button>
      </div>

      {todos.length === 0 ? (
        <p>Todo bulunamadı.</p>
      ) : (
        <ul>
          {todos.map((todo) => (
            <li key={todo.id} style={{ marginTop: "10px" }}>
              <input
                type="checkbox"
                checked={todo.completed}
                onChange={() => toggleComplete(todo)}
              />
              <span
                style={{
                  textDecoration: todo.completed ? "line-through" : "none",
                  marginLeft: "10px",
                }}
              >
                {todo.title}
              </span>
              <button
                onClick={() => deleteTodo(todo.id)}
                style={{ marginLeft: "10px" }}
              >
                Sil
              </button>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default TodoList;
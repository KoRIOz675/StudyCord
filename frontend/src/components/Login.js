import { useState, useEffect } from "react";
import { getUsers } from "../api/api";

export default function Login({ onLogin }) {
  const [users, setUsers] = useState([]);

  useEffect(() => {
    getUsers()
      .then((res) => setUsers(res.data))
      .catch(console.error);
  }, []);

  return (
    <div
      style={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "center",
        height: "100vh",
        backgroundColor: "#313338",
        color: "white",
        gap: "16px",
      }}
    >
      <h2 style={{ marginBottom: "8px" }}>👋 Who are you?</h2>
      {users.map((user) => (
        <button
          key={user.id}
          onClick={() => onLogin(user)}
          style={{
            padding: "12px 32px",
            borderRadius: "8px",
            border: "none",
            backgroundColor: "#5865f2",
            color: "white",
            cursor: "pointer",
            fontSize: "15px",
            fontWeight: "bold",
            width: "200px",
          }}
        >
          {user.username}
          <div style={{ fontSize: "11px", fontWeight: "normal", opacity: 0.8 }}>
            {user.role} · {user.school}
          </div>
        </button>
      ))}
    </div>
  );
}

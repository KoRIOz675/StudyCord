import { useEffect, useState } from "react";
import { getSuggestions } from "../api/api";

export default function Suggestions({ userId, onClose }) {
  const [suggestions, setSuggestions] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getSuggestions(userId)
      .then((res) => setSuggestions(res.data))
      .catch(console.error)
      .finally(() => setLoading(false));
  }, [userId]);

  return (
    <div
      style={{
        position: "absolute",
        right: "16px",
        top: "60px",
        width: "280px",
        backgroundColor: "#2b2d31",
        borderRadius: "8px",
        boxShadow: "0 4px 20px rgba(0,0,0,0.5)",
        zIndex: 100,
        padding: "12px",
      }}
    >
      {/* Header */}
      <div
        style={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          marginBottom: "12px",
        }}
      >
        <span style={{ fontWeight: "bold", fontSize: "14px", color: "white" }}>
          🔍 Suggested Servers
        </span>
        <span
          onClick={onClose}
          style={{ cursor: "pointer", color: "#6d6f78", fontSize: "18px" }}
        >
          ×
        </span>
      </div>

      {/* Contenu */}
      {loading ? (
        <div style={{ color: "#6d6f78", fontSize: "13px" }}>Loading...</div>
      ) : suggestions.length === 0 ? (
        <div style={{ color: "#6d6f78", fontSize: "13px" }}>
          No suggestions found.
        </div>
      ) : (
        suggestions.map((server) => (
          <div
            key={server.id}
            style={{
              padding: "8px 10px",
              borderRadius: "6px",
              backgroundColor: "#35373c",
              marginBottom: "6px",
            }}
          >
            <div
              style={{ color: "white", fontSize: "14px", fontWeight: "bold" }}
            >
              {server.name}
            </div>
            <div style={{ color: "#6d6f78", fontSize: "12px" }}>
              {server.school}
            </div>
          </div>
        ))
      )}

      <div style={{ marginTop: "10px", fontSize: "11px", color: "#6d6f78" }}>
        Based on members in common (BFS)
      </div>
    </div>
  );
}

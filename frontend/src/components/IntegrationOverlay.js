import { useEffect, useState } from "react";
import { getIntegrationSuggestions, joinServer } from "../api/api";

// Positive, non-intrusive welcome overlay: when the backend flags the current
// student as isolated, it gently offers active servers to join. Dismissal is
// remembered for the session so it does not pop up again in a loop.
export default function IntegrationOverlay({ userId, onJoin }) {
  const [data, setData] = useState(null);
  const [joiningId, setJoiningId] = useState(null);
  const dismissKey = `integrationOverlayDismissed_${userId}`;

  useEffect(() => {
    if (!userId) return;
    if (sessionStorage.getItem(dismissKey) === "true") return;

    getIntegrationSuggestions(userId)
      .then((res) => {
        if (res.data && res.data.isolated) {
          setData(res.data);
        }
      })
      .catch(console.error);
  }, [userId, dismissKey]);

  if (!data) return null;

  const close = () => {
    sessionStorage.setItem(dismissKey, "true");
    setData(null);
  };

  // Join the server, then let the parent navigate to it.
  const join = (server) => {
    setJoiningId(server.serverId);
    joinServer(userId, server.serverId)
      .then(() => {
        sessionStorage.setItem(dismissKey, "true");
        setData(null);
        if (onJoin) onJoin(server);
      })
      .catch(console.error)
      .finally(() => setJoiningId(null));
  };

  return (
    <div
      onClick={close}
      style={{
        position: "fixed",
        inset: 0,
        backgroundColor: "rgba(0,0,0,0.5)",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        zIndex: 1000,
      }}
    >
      <div
        onClick={(e) => e.stopPropagation()}
        style={{
          width: "360px",
          maxWidth: "90vw",
          backgroundColor: "#2b2d31",
          color: "white",
          borderRadius: "10px",
          boxShadow: "0 8px 30px rgba(0,0,0,0.6)",
          padding: "20px",
        }}
      >
        <h3 style={{ margin: "0 0 8px" }}>Welcome to StudyCord 👋</h3>
        <p style={{ margin: "0 0 16px", color: "#b5bac1", fontSize: "14px" }}>
          {data.message ||
            "Here are some active servers you can join to start chatting."}
        </p>

        {data.recommendedServers.map((server) => (
          <div
            key={server.serverId}
            style={{
              padding: "10px 12px",
              borderRadius: "6px",
              backgroundColor: "#35373c",
              marginBottom: "8px",
              display: "flex",
              alignItems: "center",
              justifyContent: "space-between",
              gap: "8px",
            }}
          >
            <div style={{ minWidth: 0 }}>
              <div style={{ fontWeight: "bold", fontSize: "14px" }}>
                {server.serverName}
              </div>
              <div style={{ color: "#b5bac1", fontSize: "12px" }}>
                {server.subject || "—"}
                {server.school ? ` · ${server.school}` : ""}
              </div>
              <div
                style={{ color: "#6d6f78", fontSize: "11px", marginTop: "2px" }}
              >
                {server.memberCount} members · {server.messageCount} messages
              </div>
            </div>
            <button
              onClick={() => join(server)}
              disabled={joiningId === server.serverId}
              style={{
                flexShrink: 0,
                padding: "8px 12px",
                borderRadius: "6px",
                border: "none",
                backgroundColor: "#248046",
                color: "white",
                fontWeight: "bold",
                fontSize: "12px",
                cursor: joiningId === server.serverId ? "default" : "pointer",
                opacity: joiningId === server.serverId ? 0.6 : 1,
              }}
            >
              {joiningId === server.serverId ? "…" : "Join"}
            </button>
          </div>
        ))}

        <button
          onClick={close}
          style={{
            marginTop: "8px",
            width: "100%",
            padding: "10px",
            borderRadius: "6px",
            border: "none",
            backgroundColor: "#5865f2",
            color: "white",
            fontWeight: "bold",
            cursor: "pointer",
          }}
        >
          Later
        </button>
      </div>
    </div>
  );
}

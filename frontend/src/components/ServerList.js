import { useEffect, useState } from "react";
import { getServersByUser } from "../api/api";

export default function ServerList({ userId, onSelect, selectedServer }) {
  const [servers, setServers] = useState([]);

  useEffect(() => {
    getServersByUser(userId)
      .then((res) => setServers(res.data))
      .catch(console.error);
  }, [userId]);

  return (
    <>
      {servers.map((server) => {
        const initials = server.name.substring(0, 2).toUpperCase();
        const isSelected = selectedServer?.id === server.id;
        return (
          <div
            key={server.id}
            onClick={() => onSelect(server)}
            title={server.name}
            style={{
              width: "48px",
              height: "48px",
              borderRadius: isSelected ? "16px" : "50%",
              backgroundColor: isSelected ? "#5865f2" : "#36393f",
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              cursor: "pointer",
              fontSize: "13px",
              fontWeight: "bold",
              color: "white",
              transition: "border-radius 0.2s",
              flexShrink: 0,
            }}
          >
            {initials}
          </div>
        );
      })}
    </>
  );
}

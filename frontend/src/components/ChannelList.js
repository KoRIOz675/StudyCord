import { useEffect, useState } from "react";
import { getChannels } from "../api/api";

export default function ChannelList({ server, onSelect, selectedChannel }) {
  const [channels, setChannels] = useState([]);

  useEffect(() => {
    getChannels(server.id)
      .then((res) => setChannels(res.data))
      .catch(console.error);
  }, [server.id]);

  return (
    <div style={{ display: "flex", flexDirection: "column" }}>
      {/* Header server */}
      <div
        style={{
          padding: "16px",
          fontWeight: "bold",
          fontSize: "15px",
          borderBottom: "1px solid #1e1f22",
          color: "white",
        }}
      >
        {server.name}
      </div>

      {/* Liste channels */}
      <div style={{ padding: "8px" }}>
        <div
          style={{
            fontSize: "11px",
            color: "#6d6f78",
            padding: "8px 8px 4px",
            textTransform: "uppercase",
            letterSpacing: "0.5px",
          }}
        >
          Channels
        </div>
        {channels.map((channel) => {
          const isSelected = selectedChannel?.id === channel.id;
          return (
            <div
              key={channel.id}
              onClick={() => onSelect(channel)}
              style={{
                padding: "6px 8px",
                borderRadius: "4px",
                cursor: "pointer",
                color: isSelected ? "white" : "#949ba4",
                backgroundColor: isSelected ? "#35373c" : "transparent",
                fontSize: "14px",
              }}
            >
              # {channel.name}
            </div>
          );
        })}
      </div>
    </div>
  );
}

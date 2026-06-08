import { useEffect, useState } from "react";
import { getMessages, postMessage } from "../api/api";

export default function MessageFeed({ channel, currentUser }) {
  const [messages, setMessages] = useState([]);
  const [content, setContent] = useState("");
  const authorId = currentUser.id;

  // APRÈS
  useEffect(() => {
    getMessages(channel.id)
      .then((res) => setMessages(res.data))
      .catch(console.error);
  }, [channel.id]);

  const loadMessages = () => {
    getMessages(channel.id)
      .then((res) => setMessages(res.data))
      .catch(console.error);
  };

  const handleSend = () => {
    if (!content.trim()) return;
    postMessage(channel.id, content, authorId).then(() => {
      setContent("");
      loadMessages();
    });
  };

  return (
    <div style={{ display: "flex", flexDirection: "column", height: "100%" }}>
      {/* Header */}
      <div
        style={{
          padding: "12px 16px",
          borderBottom: "1px solid #1e1f22",
          fontWeight: "bold",
          fontSize: "15px",
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          position: "relative",
        }}
      >
        <span># {channel.name}</span>
      </div>

      {/* Messages */}
      <div
        style={{
          flex: 1,
          overflowY: "auto",
          padding: "16px",
          display: "flex",
          flexDirection: "column",
          gap: "8px",
        }}
      >
        {messages.map((msg) => (
          <div
            key={msg.id}
            style={{ display: "flex", flexDirection: "column" }}
          >
            <span style={{ fontSize: "12px", color: "#6d6f78" }}>
              {msg.author?.username || "Unknown"}
            </span>
            <span style={{ fontSize: "14px", color: "#dcddde" }}>
              {msg.content}
            </span>
          </div>
        ))}
      </div>

      {/* Input */}
      <div style={{ padding: "0 16px 16px", display: "flex", gap: "8px" }}>
        <input
          value={content}
          onChange={(e) => setContent(e.target.value)}
          onKeyDown={(e) => e.key === "Enter" && handleSend()}
          placeholder={`Message #${channel.name}`}
          style={{
            flex: 1,
            padding: "10px 14px",
            borderRadius: "8px",
            border: "none",
            backgroundColor: "#383a40",
            color: "white",
            fontSize: "14px",
            outline: "none",
          }}
        />
        <button
          onClick={handleSend}
          style={{
            padding: "10px 16px",
            borderRadius: "8px",
            border: "none",
            backgroundColor: "#5865f2",
            color: "white",
            cursor: "pointer",
            fontWeight: "bold",
          }}
        >
          Send
        </button>
      </div>
    </div>
  );
}

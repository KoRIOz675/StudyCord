import { useEffect, useState } from "react";
import { getMessages, postMessage, getSimilarMessages } from "../api/api";

export default function MessageFeed({ channel, currentUser }) {
  const [messages, setMessages] = useState([]);
  const [content, setContent] = useState("");
  const [searchOpen, setSearchOpen] = useState(false);
  const [searchQuery, setSearchQuery] = useState("");
  const [similarIds, setSimilarIds] = useState(new Set());
  const [dupWarning, setDupWarning] = useState([]);
  const authorId = currentUser.id;

  useEffect(() => {
    getMessages(channel.id)
      .then((res) => setMessages(res.data))
      .catch(console.error);
    setSearchOpen(false);
    setSearchQuery("");
    setSimilarIds(new Set());
    setDupWarning([]);
  }, [channel.id]);

  const loadMessages = () => {
    getMessages(channel.id)
      .then((res) => setMessages(res.data))
      .catch(console.error);
  };

  const handleSend = () => {
    if (!content.trim()) return;
    const sentContent = content;
    postMessage(channel.id, sentContent, authorId).then(() => {
      setContent("");
      loadMessages();
      if (sentContent.trim().split(/\s+/).length > 5) {
        getSimilarMessages(channel.id, sentContent)
          .then((res) => {
            const similar = res.data.filter((msg) => msg.content !== sentContent);
            if (similar.length > 0) setDupWarning(similar.slice(0, 3));
          })
          .catch(console.error);
      }
    });
  };

  const handleSearch = () => {
    if (!searchQuery.trim()) return;
    getSimilarMessages(channel.id, searchQuery)
      .then((res) => setSimilarIds(new Set(res.data.map((m) => m.id))))
      .catch(console.error);
  };

  const clearSearch = () => {
    setSearchQuery("");
    setSimilarIds(new Set());
    setSearchOpen(false);
  };

  const searchActive = similarIds.size > 0;

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
        }}
      >
        <span># {channel.name}</span>
        <button
          onClick={() => (searchOpen ? clearSearch() : setSearchOpen(true))}
          title="Search similar messages"
          style={{
            background: "none",
            border: "none",
            color: searchOpen ? "#5865f2" : "#6d6f78",
            cursor: "pointer",
            fontSize: "16px",
            padding: "4px 8px",
            borderRadius: "4px",
          }}
        >
          🔎
        </button>
      </div>

      {/* Search bar */}
      {searchOpen && (
        <div
          style={{
            padding: "8px 16px",
            borderBottom: "1px solid #1e1f22",
            backgroundColor: "#2b2d31",
            display: "flex",
            gap: "8px",
            alignItems: "center",
          }}
        >
          <input
            autoFocus
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            onKeyDown={(e) => e.key === "Enter" && handleSearch()}
            placeholder="Search similar messages…"
            style={{
              flex: 1,
              padding: "6px 12px",
              borderRadius: "6px",
              border: "none",
              backgroundColor: "#383a40",
              color: "white",
              fontSize: "13px",
              outline: "none",
            }}
          />
          <button
            onClick={handleSearch}
            style={{
              padding: "6px 12px",
              borderRadius: "6px",
              border: "none",
              backgroundColor: "#5865f2",
              color: "white",
              cursor: "pointer",
              fontSize: "13px",
            }}
          >
            Search
          </button>
          {searchActive && (
            <button
              onClick={clearSearch}
              style={{
                padding: "6px 12px",
                borderRadius: "6px",
                border: "none",
                backgroundColor: "#383a40",
                color: "#dcddde",
                cursor: "pointer",
                fontSize: "13px",
              }}
            >
              Clear
            </button>
          )}
        </div>
      )}

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
        {messages.map((msg) => {
          const isMatch = similarIds.has(msg.id);
          return (
            <div
              key={msg.id}
              style={{
                display: "flex",
                flexDirection: "column",
                borderLeft: isMatch ? "3px solid #5865f2" : "3px solid transparent",
                paddingLeft: "8px",
                opacity: searchActive && !isMatch ? 0.4 : 1,
                transition: "opacity 0.15s",
              }}
            >
              <div style={{ display: "flex", alignItems: "center", gap: "6px" }}>
                <span style={{ fontSize: "12px", color: "#6d6f78" }}>
                  {msg.author?.username || "Unknown"}
                </span>
                {isMatch && (
                  <span
                    style={{
                      fontSize: "10px",
                      backgroundColor: "#5865f2",
                      color: "white",
                      padding: "1px 5px",
                      borderRadius: "3px",
                      fontWeight: "bold",
                    }}
                  >
                    Similar
                  </span>
                )}
              </div>
              <span style={{ fontSize: "14px", color: "#dcddde" }}>
                {msg.content}
              </span>
            </div>
          );
        })}
      </div>

      {/* Duplicate warning banner */}
      {dupWarning.length > 0 && (
        <div
          style={{
            margin: "0 16px 8px",
            padding: "10px 12px",
            backgroundColor: "#f0b132",
            borderRadius: "8px",
            color: "#1a1a1a",
            fontSize: "13px",
            position: "relative",
          }}
        >
          <button
            onClick={() => setDupWarning([])}
            style={{
              position: "absolute",
              top: "6px",
              right: "8px",
              background: "none",
              border: "none",
              cursor: "pointer",
              fontSize: "16px",
              color: "#1a1a1a",
              lineHeight: 1,
            }}
          >
            ×
          </button>
          <div style={{ fontWeight: "bold", marginBottom: "6px" }}>
            ⚠️ Similar messages already exist in this channel:
          </div>
          {dupWarning.map((msg, i) => (
            <div
              key={i}
              style={{
                marginTop: "3px",
                paddingLeft: "8px",
                borderLeft: "2px solid rgba(0,0,0,0.25)",
                fontSize: "12px",
              }}
            >
              "{msg.content}"
            </div>
          ))}
        </div>
      )}

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

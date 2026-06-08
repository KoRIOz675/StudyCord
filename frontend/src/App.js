import { useState } from "react";
import Login from "./components/Login";
import ServerList from "./components/ServerList";
import ChannelList from "./components/ChannelList";
import MessageFeed from "./components/MessageFeed";
import IntegrationOverlay from "./components/IntegrationOverlay";
import "./App.css";

function App() {
  const [currentUser, setCurrentUser] = useState(null);
  const [selectedServer, setSelectedServer] = useState(null);
  const [selectedChannel, setSelectedChannel] = useState(null);
  const [serverRefresh, setServerRefresh] = useState(0);

  if (!currentUser) {
    return <Login onLogin={setCurrentUser} />;
  }

  return (
    <div
      style={{
        display: "flex",
        height: "100vh",
        backgroundColor: "#313338",
        color: "white",
      }}
    >
      <IntegrationOverlay
        userId={currentUser.id}
        onJoin={(server) => {
          setSelectedServer({ id: server.serverId, name: server.serverName });
          setSelectedChannel(null);
          setServerRefresh((n) => n + 1);
        }}
      />

      {/* Sidebar servers */}
      <div
        style={{
          width: "70px",
          backgroundColor: "#1e1f22",
          padding: "8px 0",
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          gap: "8px",
        }}
      >
        <ServerList
          userId={currentUser.id}
          refreshKey={serverRefresh}
          onSelect={(server) => {
            setSelectedServer(server);
            setSelectedChannel(null);
          }}
          selectedServer={selectedServer}
        />
      </div>

      {/* Sidebar channels */}
      <div
        style={{
          width: "240px",
          backgroundColor: "#2b2d31",
          display: "flex",
          flexDirection: "column",
        }}
      >
        {/* Header user */}
        <div
          style={{
            padding: "12px 16px",
            borderBottom: "1px solid #1e1f22",
            fontSize: "13px",
            color: "#b5bac1",
          }}
        >
          <span style={{ fontWeight: "bold", color: "white" }}>
            {currentUser.username}
          </span>
          <span style={{ marginLeft: "6px", fontSize: "11px" }}>
            {currentUser.role}
          </span>
        </div>
        {selectedServer ? (
          <ChannelList
            server={selectedServer}
            onSelect={setSelectedChannel}
            selectedChannel={selectedChannel}
          />
        ) : (
          <div style={{ padding: "16px", color: "#6d6f78", fontSize: "13px" }}>
            Select a server
          </div>
        )}
      </div>

      {/* Zone principale */}
      <div
        style={{
          flex: 1,
          display: "flex",
          flexDirection: "column",
          overflow: "hidden",
        }}
      >
        {selectedChannel ? (
          <MessageFeed channel={selectedChannel} currentUser={currentUser} />
        ) : (
          <div style={{ margin: "auto", color: "#6d6f78", fontSize: "14px" }}>
            Select a channel to start
          </div>
        )}
      </div>
    </div>
  );
}

export default App;

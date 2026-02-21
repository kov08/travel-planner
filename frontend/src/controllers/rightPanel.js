import { useState } from "react";
import { useLazyQuery } from "@apollo/client/react";
import { FIND_OPTIMAL_PATH } from "../graphql";
import { useEffect } from "react";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";

function RightPanel() {
  const [form, setForm] = useState({
    source: "",
    destination: "",
    criteria: "COST",
  });
  const [emailMessage, setEmailMessage] = useState("");

  const [findPath, { data, loading, error }] = useLazyQuery(FIND_OPTIMAL_PATH);

  const handleSubmit = () => {
    findPath({
      variables: {
        source: form.source,
        destination: form.destination,
        criteria: form.criteria,
      },
    });
  };

  useEffect(() => {
    const socket = new SockJS("http://localhost:8080/ws");

    const client = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      onConnect: () => {
        console.log("Connected to WebSocket");

        client.subscribe("/topic/email-status", (message) => {
          setEmailMessage(message.body);
        });
      },
    });

    client.activate();

    return () => {
      client.deactivate();
    };
  }, []);

  return (
    <>
      <h3>Route Calculation</h3>

      <input
        placeholder="Source"
        value={form.source}
        onChange={(e) => setForm({ ...form, source: e.target.value })}
      />

      <input
        placeholder="Destination"
        value={form.destination}
        onChange={(e) => setForm({ ...form, destination: e.target.value })}
      />

      <select
        value={form.criteria}
        onChange={(e) => setForm({ ...form, criteria: e.target.value })}
      >
        <option value="COST">COST</option>
        <option value="TIME">TIME</option>
      </select>

      <button onClick={handleSubmit}>Calculate</button>

      {loading && <p>Calculating...</p>}
      {error && <p>Error: {error.message}</p>}

      {data?.findOptimalPath && (
        <div style={{ marginTop: "20px" }}>
          <h4>Result</h4>
          <p>
            <strong>Path:</strong> {data.findOptimalPath.path.join(" → ")}
          </p>
          <p>
            <strong>Total Cost:</strong> {data.findOptimalPath.totalCost}
          </p>
          <p>
            <strong>Total Time:</strong> {data.findOptimalPath.totalTime}
          </p>
        </div>
      )}
      {emailMessage && (
        <div
          style={{
            marginTop: "20px",
            padding: "10px",
            border: "2px solid green",
            backgroundColor: "#2b8b2b",
          }}
        >
          <h4>Email Service Status</h4>
          <p>{emailMessage}</p>
        </div>
      )}
    </>
  );
}

export default RightPanel;

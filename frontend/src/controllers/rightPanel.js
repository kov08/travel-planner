import { useState } from "react";
import { useLazyQuery } from "@apollo/client/react";
import { FIND_OPTIMAL_PATH } from "../graphql";

function RightPanel() {
  const [form, setForm] = useState({
    source: "",
    destination: "",
    criteria: "COST",
  });

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
    </>
  );
}

export default RightPanel;

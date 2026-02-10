import { useState } from "react";
import { useMutation, useQuery } from "@apollo/client/react";
import { ADD_CONNECTION, GET_CONNECTIONS, DELETE_CONNECTION } from "../graphql";

function LeftPanel() {
  const [form, setForm] = useState({
    source: "",
    destination: "",
    cost: "",
    time: "",
    mode: "",
  });

  // const { data, refetch } = useQuery(GET_CONNECTIONS);
  const { data, loading, error, refetch } = useQuery(GET_CONNECTIONS);
  const [addConnection] = useMutation(ADD_CONNECTION);
  const [deleteConnection] = useMutation(DELETE_CONNECTION);

  const handleSubmit = async () => {
    await addConnection({
      variables: {
        ...form,
        cost: parseFloat(form.cost),
        time: parseFloat(form.time),
      },
    });
    setForm({ source: "", destination: "", cost: "", time: "", mode: "" });
    refetch();
  };

  if (loading) return <p>Loading...</p>;
  if (error) return <p>Error: {error.message}</p>;

  return (
    <>
      <h3>Add Travel Data</h3>

      {Object.keys(form).map((key) => (
        <input
          key={key}
          placeholder={key}
          value={form[key]}
          onChange={(e) => setForm({ ...form, [key]: e.target.value })}
        />
      ))}

      <button onClick={handleSubmit}>Submit</button>

      <h3>Stored Routes</h3>
      <table>
        <thead>
          <tr>
            <th>#</th>
            <th>Source</th>
            <th>Destination</th>
            <th>Cost</th>
            <th>Time</th>
            <th>Mode</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {data?.getAllConnections.map((row, i) => (
            <tr key={row.id}>
              <td>{i + 1}</td>
              <td>{row.source}</td>
              <td>{row.destination}</td>
              <td>{row.cost}</td>
              <td>{row.time}</td>
              <td>{row.mode}</td>
              <td>
                <button
                  onClick={async () => {
                    await deleteConnection({ variables: { id: row.id } });
                    refetch();
                  }}
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </>
  );
}

export default LeftPanel;

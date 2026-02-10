import logo from "./logo.svg";
import "./App.css";
import LeftPanel from "./controllers/leftPanel";

function App() {
  return (
    <div className="container">
      <div className="left">
        <LeftPanel />
      </div>
      <div className="right">
        <h3>Route Calculation (Coming Soon)</h3>
      </div>
    </div>
  );
}

export default App;

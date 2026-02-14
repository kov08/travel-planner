import logo from "./logo.svg";
import "./App.css";
import LeftPanel from "./controllers/leftPanel";
import RightPanel from "./controllers/rightPanel";

function App() {
  return (
    <div className="container">
      <div className="left">
        <LeftPanel />
      </div>
      <div className="right">
        <RightPanel />
      </div>
    </div>
  );
}

export default App;

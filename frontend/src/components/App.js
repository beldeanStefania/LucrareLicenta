import logo from "../logo.svg";
import "./App.css";

import Header from "./Header";
import AppContent from "./AppContent";

function App() {
  return (
    <div>
      <Header pageTitle="Frontend authenticated with JWT" logoSrc={logo} />
      <AppContent />
    </div>
  );
}

export default App;

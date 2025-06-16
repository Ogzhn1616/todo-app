import { useState } from "react";
import Login from "./components/Login";
import Register from "./components/Register";
import TodoList from "./components/TodoList";

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [username, setUsername] = useState("");
  const [showRegister, setShowRegister] = useState(false);

  const handleLogin = (username) => {
    setUsername(username);
    setIsAuthenticated(true);
  };

  const handleLogout = () => {
    localStorage.removeItem("token");
    setIsAuthenticated(false);
    setUsername("");
  };

  return (
    <div>
      {isAuthenticated ? (
        <TodoList username={username} onLogout={handleLogout} />
      ) : showRegister ? (
        <Register
          onRegisterSuccess={() => setShowRegister(false)}
          switchToLogin={() => setShowRegister(false)}
        />
      ) : (
        <Login
          onLogin={handleLogin}
          switchToRegister={() => setShowRegister(true)}
        />
        
      )}
    </div>
  );
}

export default App;
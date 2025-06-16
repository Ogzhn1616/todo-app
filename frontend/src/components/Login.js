import { useState } from "react";

function Login({ onLogin, switchToRegister }) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");

  const handleLogin = async () => {
    const res = await fetch("http://localhost:8080/api/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password }),
    });

    if (res.ok) {
      const data = await res.json();
      localStorage.setItem("token", data.token);
      setMessage("Giriş başarılı!");
      onLogin(); 
    } else {
      setMessage("Giriş başarısız.");
    }
  };

  return (
    <div>
      <h1>TodoApp Login</h1>
      <input
        type="text"
        placeholder="Kullanıcı adı"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
      /><br />
      <input
        type="password"
        placeholder="Şifre"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      /><br />
      <button onClick={handleLogin}>Giriş Yap</button>

      <p>
        Hesabın yok mu?{" "}
        <button onClick={switchToRegister}>Kayıt Ol</button>
      </p>

      {message && <p>{message}</p>}
    </div>
  );
}

export default Login;
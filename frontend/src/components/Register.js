import { useState } from "react";

function Register({ onRegisterSuccess, switchToLogin }) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");

  const handleRegister = async () => {
    const res = await fetch("http://localhost:8080/api/auth/register", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password }),
    });

    if (res.ok) {
      setMessage("Kayıt başarılı! Giriş yapabilirsin.");
      onRegisterSuccess(); 
    } else {
      setMessage("Kayıt başarısız. Kullanıcı adı alınmış olabilir.");
    }
  };

  return (
    <div>
      <h1>Kayıt Ol</h1>
      <input
        type="text"
        placeholder="Kullanıcı adı"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
      /><br/>
      <input
        type="password"
        placeholder="Şifre"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      /><br/>
      <button onClick={handleRegister}>Kayıt Ol</button>
      <p>
        Zaten hesabın var mı?{" "}
        <button onClick={switchToLogin}>Giriş Yap</button>
      </p>
      {message && <p>{message}</p>}
    </div>
  );
}

export default Register;
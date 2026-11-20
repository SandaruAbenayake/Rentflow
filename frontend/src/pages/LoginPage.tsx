import { Alert, Box, Button, Card, CardContent, TextField, Typography } from "@mui/material";
import { useState } from "react";
import { useAuth } from "../auth/AuthContext";

export default function LoginPage() {
  const { login } = useAuth();
  const [email,setEmail] = useState("");
  const [password,setPassword] = useState("");
  const [error,setError] = useState("");
  const [busy,setBusy] = useState(false);
  async function submit(event: React.FormEvent) {
    event.preventDefault(); setBusy(true); setError("");
    try { await login(email,password); } catch (e) { setError(e instanceof Error ? e.message : "Login failed"); }
    finally { setBusy(false); }
  }
  return (
    <Box sx={{ minHeight:"100vh", display:"grid", placeItems:"center", background:"#f4f7fb" }}>
      <Card sx={{ width:{xs:"calc(100% - 32px)",sm:400} }}>
        <CardContent component="form" onSubmit={submit} sx={{ p:4 }}>
          <Typography variant="h5" sx={{ mb:3 }}>Sign in to RentFlow</Typography>
          {error && <Alert severity="error" sx={{ mb:2 }}>{error}</Alert>}
          <TextField fullWidth label="Email" type="email" value={email} onChange={e=>setEmail(e.target.value)} margin="normal" required />
          <TextField fullWidth label="Password" type="password" value={password} onChange={e=>setPassword(e.target.value)} margin="normal" required />
          <Button fullWidth variant="contained" type="submit" disabled={busy} sx={{ mt:2 }}>{busy?"Signing in…":"Sign in"}</Button>
        </CardContent>
      </Card>
    </Box>
  );
}

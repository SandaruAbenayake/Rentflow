import { Alert, Box, Button, CircularProgress, Dialog, DialogActions, DialogContent, DialogTitle, Stack, TextField, Typography } from "@mui/material";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import { api } from "../api/client";

type Kind = "companies"|"vehicles"|"drivers"|"customers"|"bookings"|"payments"|"trips";
const fields: Record<Kind,string[]> = {
  companies:["name","code","email","phone","address","adminEmail","adminPassword","adminFullName"],
  vehicles:["registrationNumber","make","model","year","color","transmission","fuelType","seatingCapacity","dailyRate","description"],
  drivers:["fullName","phone","email","licenseNumber","status"],
  customers:["fullName","email","phone","address","identificationType","identificationNumber"],
  bookings:["customerId","vehicleId","driverId","startAt","endAt","dailyRateSnapshot","depositAmount","notes"],
  payments:["bookingId","amount","paymentMethod","status","transactionReference","notes"],
  trips:["bookingId","vehicleId","driverId","tripType","status","startAt","endAt","startLocation","endLocation"],
};
export default function DataPage({kind}:{kind:Kind}) {
  const [open,setOpen] = useState(false);
  const [form,setForm] = useState<Record<string,string>>({});
  const [error,setError] = useState("");
  const queryClient = useQueryClient();
  const query = useQuery({queryKey:[kind],queryFn:async()=>{const result=await api.get(`/${kind}`);return result.data;}});
  const mutation = useMutation({
    mutationFn: async()=> {
      const payload=Object.fromEntries(Object.entries(form).filter(([,v])=>v!==""));
      await api.post(`/${kind}`,payload);
    },
    onSuccess:()=>{setOpen(false);setForm({});setError("");void queryClient.invalidateQueries({queryKey:[kind]});},
    onError:(e)=>setError(e instanceof Error?e.message:"Operation failed"),
  });
  const rows=Array.isArray(query.data)?query.data:[];
  return (
    <Box>
      <Stack direction="row" alignItems="center" justifyContent="space-between" sx={{ mb:2 }}>
        <Typography variant="h5" sx={{ textTransform:"capitalize" }}>{kind}</Typography>
        <Button variant="contained" onClick={()=>setOpen(true)}>Add {kind.slice(0,-1)}</Button>
      </Stack>
      {query.isLoading&&<CircularProgress />}
      {query.isError&&<Alert severity="error">Unable to load {kind}</Alert>}
      {!query.isLoading&&!rows.length&&<Alert severity="info">No {kind} records yet.</Alert>}
      {rows.map((row:any)=>(
        <Box key={row.id} sx={{ p:2, mb:1, border:"1px solid",borderColor:"divider",borderRadius:1 }}>
          <Typography>{row.name||row.fullName||row.registrationNumber||row.id}</Typography>
          <Typography variant="body2" color="text.secondary">{row.status||row.email||row.make||row.paymentMethod||""}</Typography>
        </Box>
      ))}
      <Dialog open={open} onClose={()=>setOpen(false)} fullWidth maxWidth="sm">
        <DialogTitle>Add {kind.slice(0,-1)}</DialogTitle>
        <DialogContent>
          {error&&<Alert severity="error" sx={{ mt:1,mb:1 }}>{error}</Alert>}
          <Stack spacing={2} sx={{ mt:1 }}>
            {fields[kind].map(field=><TextField key={field} label={field} value={form[field]||""}
              onChange={event=>setForm({...form,[field]:event.target.value})}
              type={field.includes("Password")?"password":field.includes("Email")?"email":"text"} fullWidth />)}
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={()=>setOpen(false)}>Cancel</Button>
          <Button variant="contained" onClick={()=>mutation.mutate()} disabled={mutation.isPending}>Save</Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}

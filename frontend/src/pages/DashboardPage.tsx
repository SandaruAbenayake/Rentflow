import { Card, CardContent, Grid, Typography } from "@mui/material";
import { useQuery } from "@tanstack/react-query";
import { api } from "../api/client";
import { useAuth } from "../auth/AuthContext";

export default function DashboardPage() {
  const { principal } = useAuth();
  const vehicles = useQuery({ queryKey:["dashboard","vehicles"], queryFn: async()=> (await api.get("/vehicles")).data, enabled:principal?.role !== "SUPER_ADMIN" });
  const customers = useQuery({ queryKey:["dashboard","customers"], queryFn: async()=> (await api.get("/customers")).data, enabled:principal?.role !== "SUPER_ADMIN" });
  const bookings = useQuery({ queryKey:["dashboard","bookings"], queryFn: async()=> (await api.get("/bookings")).data, enabled:principal?.role !== "SUPER_ADMIN" });
  const payments = useQuery({ queryKey:["dashboard","payments"], queryFn: async()=> (await api.get("/payments")).data, enabled:principal?.role !== "SUPER_ADMIN" });
  const vehicleRows = Array.isArray(vehicles.data) ? vehicles.data : [];
  const bookingRows = Array.isArray(bookings.data) ? bookings.data : [];
  const paymentRows = Array.isArray(payments.data) ? payments.data : [];
  const metrics = [
    ["Vehicles", vehicleRows.length],
    ["Available", vehicleRows.filter(v=>v.status==="AVAILABLE").length],
    ["Active rentals", bookingRows.filter(b=>b.status==="ACTIVE").length],
    ["Customers", Array.isArray(customers.data)?customers.data.length:0],
    ["Bookings", bookingRows.length],
    ["Collected payments", paymentRows.filter(p=>p.status==="PAID").reduce((sum,p)=>sum+Number(p.amount||0),0).toFixed(2)],
  ];
  return (
    <Grid container spacing={2}>
      {metrics.map(([label,value]) => (
        <Grid key={String(label)} size={{xs:12,sm:6,md:4}}>
          <Card><CardContent><Typography color="text.secondary">{label}</Typography><Typography variant="h4">{value}</Typography></CardContent></Card>
        </Grid>
      ))}
    </Grid>
  );
}

import { AppBar, Box, Button, Drawer, List, ListItemButton, ListItemText, Toolbar, Typography } from "@mui/material";
import { NavLink, Outlet } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";

const width = 240;
export default function ShellLayout() {
  const { principal, logout } = useAuth();
  const items = [
    ["Dashboard", "/"],
    ...(principal?.role === "SUPER_ADMIN" ? [["Companies", "/companies"]] : []),
    ...(principal?.role !== "SUPER_ADMIN" ? [["Vehicles","/vehicles"],["Drivers","/drivers"],["Customers","/customers"],["Bookings","/bookings"],["Payments","/payments"],["Trips","/trips"]] : []),
  ];
  return (
    <Box sx={{ display:"flex", minHeight:"100vh" }}>
      <AppBar position="fixed" sx={{ zIndex: theme => theme.zIndex.drawer + 1 }}>
        <Toolbar>
          <Typography variant="h6" sx={{ flexGrow:1 }}>RentFlow</Typography>
          <Typography sx={{ mr:2 }}>{principal?.fullName}</Typography>
          <Button color="inherit" onClick={() => void logout()}>Logout</Button>
        </Toolbar>
      </AppBar>
      <Drawer variant="permanent" sx={{ width, "& .MuiDrawer-paper": { width, boxSizing:"border-box" } }}>
        <Toolbar />
        <List>
          {items.map(([label,path]) => (
            <ListItemButton component={NavLink} to={path} key={path}>
              <ListItemText primary={label} />
            </ListItemButton>
          ))}
        </List>
      </Drawer>
      <Box component="main" sx={{ flexGrow:1, p:3 }}>
        <Toolbar />
        <Outlet />
      </Box>
    </Box>
  );
}

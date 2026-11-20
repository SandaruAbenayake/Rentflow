import { Alert, Box, Button } from "@mui/material";
import { Link } from "react-router-dom";
export default function UnauthorizedPage() {
  return <Box sx={{ p:4 }}><Alert severity="warning" sx={{ mb:2 }}>You are not authorized to access this page.</Alert><Button component={Link} to="/">Return to dashboard</Button></Box>;
}

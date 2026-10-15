import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import './index.css';
import App from './App.tsx';
import AuthProvider from './providers/authProvider.tsx';
import SnackbarProvider from './providers/snackbarProvider.tsx';
import {ThemeProvider} from "@mui/material";
import theme from './theme/theme.ts'
import { CssBaseline} from '@mui/material';

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <AuthProvider>
      <SnackbarProvider>
          <ThemeProvider  theme={theme}>
              <CssBaseline/>
              <App/>
          </ThemeProvider>
      </SnackbarProvider>
    </AuthProvider>
  </StrictMode>
);

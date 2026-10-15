import './Header.css';
import {
  AppBar, Box, Button, Drawer, IconButton, List, ListItem, ListItemButton, ListItemText, Toolbar, Typography
} from '@mui/material';
import MenuIcon from '@mui/icons-material/Menu';
import { Link } from 'react-router';
import { useState } from 'react';
import AuthToggle from '../../auth/AuthToggle/AuthToggle.tsx';
import useAuth from '../../../../hooks/useAuth.ts';
import type { Role } from '../../../../api/types/user.ts';

import { useLocation } from 'react-router';
import VezStrip from '../Vez/Vez.tsx';
import { colors, fonts } from '../../../../theme/theme.ts';

interface Page {
  path: string;
  name: string;
  authenticated: boolean;
  role?: Role;
}

const pages: Page[] = [
  { path: '/', name: 'почетна', authenticated: false },
  { path: '/search-runs', name: 'пребарување', authenticated: true },
  { path: '/resources', name: 'статии', authenticated: true },
  { path: '/donations', name: 'донации', authenticated: true },
  { path: '/playground', name: 'mcp', authenticated: true },
    { path: '/negotiation', name: 'агент', authenticated: true }
];

const Header = () => {
  const [drawerOpen, setDrawerOpen] = useState(false);
  const { pathname } = useLocation();

  const { isLoggedIn, user } = useAuth();
  const visiblePages = pages.filter((page) =>
    (!page.authenticated || isLoggedIn) &&
    (!page.role || (user?.roles.includes(page.role) ?? false))
  );

  return (
    <Box>
        <AppBar position='static' sx={{ backgroundColor: colors.brown }}>
            <Toolbar sx={{ display: 'flex' }}>
                <IconButton
                    size='large' edge='start' color='inherit' aria-label='menu'
                    sx={{ mr: 2, display: { md: 'none' } }}
                    onClick={() => setDrawerOpen(true)}
                >
                    <MenuIcon/>
                </IconButton>

                <Typography component='div' sx={{ mr: 4, fontFamily: fonts.display, fontWeight: 900, fontSize: 18 }}>
                    КОРПУС <Box component='span' sx={{ color: colors.red }}>×</Box> ВЕЗИЛКА
                </Typography>

                <Box sx={{ flexGrow: 1, display: { xs: 'none', md: 'flex' }, gap: 1 }}>
                    {visiblePages.map((page) => {
                        const active = page.path === '/' ? pathname === '/' : pathname.startsWith(page.path);
                        return (
                            <Link key={page.name} to={page.path}>
                                <Button sx={{
                                    color: active ? colors.blush : colors.cream,
                                    fontFamily: fonts.body, fontWeight: 800, fontSize: 12, letterSpacing: '0.14em'
                                }}>
                                    {page.name.toUpperCase()}
                                </Button>
                            </Link>
                        );
                    })}
                </Box>

                <Box sx={{ flexGrow: 1, display: 'flex', justifyContent: 'flex-end' }}>
                    <AuthToggle/>
                </Box>
            </Toolbar>
        </AppBar>
        <VezStrip/>

      <Drawer anchor='left' open={drawerOpen} onClose={() => setDrawerOpen(false)}>
        <Box sx={{ width: 240 }} role='presentation' onClick={() => setDrawerOpen(false)}>
          <List>
            {visiblePages.map((page) => (
              <ListItem key={page.name} disablePadding>
                <ListItemButton component={Link} to={page.path}>
                  <ListItemText primary={page.name} sx={{ textTransform: 'capitalize' }}/>
                </ListItemButton>
              </ListItem>
            ))}
          </List>
        </Box>
      </Drawer>
    </Box>
  );
};

export default Header;

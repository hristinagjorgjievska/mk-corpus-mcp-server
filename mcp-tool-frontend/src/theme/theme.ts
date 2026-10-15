import { createTheme } from '@mui/material';

export const colors = {
    red: '#A02222',
    brown: '#281A15',
    cream: '#F9F7F5',
    linen: '#EFE7DC',
    muted: '#6E5A52',
    pale: '#D7C8BB',
    blush: '#E9B7AE'
};

export const fonts = {
    display: '"Manrope", sans-serif',
    body: '"Manrope", sans-serif',
    voice: '"Manrope", sans-serif'
};

const theme = createTheme({
    palette: {
        primary: { main: colors.red },
        secondary: { main: colors.brown },
        background: { default: colors.cream, paper: colors.cream },
        text: { primary: colors.brown, secondary: colors.muted }
    },
    shape: { borderRadius: 0 },
    typography: {
        fontFamily: fonts.body,
        h1: { fontFamily: fonts.display, fontWeight: 800 },
        h2: { fontFamily: fonts.display, fontWeight: 800 },
        h3: { fontFamily: fonts.display, fontWeight: 800 },
        h4: { fontFamily: fonts.display, fontWeight: 800 },
        h5: { fontFamily: fonts.display, fontWeight: 800 },
        h6: { fontFamily: fonts.display, fontWeight: 800 },
        button: { fontFamily: fonts.display, fontWeight: 800, letterSpacing: 0 }
    },
    components: {
        MuiButton: { defaultProps: { disableElevation: true } },
        MuiPaper: { defaultProps: { elevation: 0 } },
        MuiCard: { defaultProps: { elevation: 0 } },
        MuiAppBar: { defaultProps: { elevation: 0 } }
    }
});

export default theme;
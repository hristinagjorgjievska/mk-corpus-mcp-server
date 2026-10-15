import { Box } from '@mui/material';
import { colors } from '../../../../theme/theme.ts';

const motif = `<svg xmlns='http://www.w3.org/2000/svg' width='20' height='10' viewBox='0 0 20 10'>
  <rect x='8' y='0' width='4' height='2' fill='${colors.red}'/>
  <rect x='4' y='2' width='4' height='2' fill='${colors.red}'/><rect x='12' y='2' width='4' height='2' fill='${colors.red}'/>
  <rect x='0' y='4' width='4' height='2' fill='${colors.red}'/><rect x='8' y='4' width='4' height='2' fill='${colors.cream}'/><rect x='16' y='4' width='4' height='2' fill='${colors.red}'/>
  <rect x='4' y='6' width='4' height='2' fill='${colors.red}'/><rect x='12' y='6' width='4' height='2' fill='${colors.red}'/>
  <rect x='8' y='8' width='4' height='2' fill='${colors.red}'/>
</svg>`;

const VezStrip = () => (
    <Box
        aria-hidden
        sx={{
            height: 10,
            backgroundColor: colors.brown,
            backgroundImage: `url("data:image/svg+xml,${encodeURIComponent(motif)}")`,
            backgroundRepeat: 'repeat-x'
        }}
    />
);

export default VezStrip;
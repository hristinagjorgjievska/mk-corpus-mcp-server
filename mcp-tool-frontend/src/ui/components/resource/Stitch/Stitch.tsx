import { Box } from '@mui/material';
import { colors, fonts } from '../../../../theme/theme.ts';

interface StitchProps {
    value: number | null;
    light?: boolean;
}

const Stitch = ({ value, light = false }: StitchProps) => {
    const filled = Math.round((value ?? 0) * 10);
    const onColor = light ? colors.cream : colors.red;

    return (
        <Box component='span' title={`МК ${(value ?? 0).toFixed(2)}`}
             sx={{ fontFamily: fonts.display, fontWeight: 700, fontSize: 12, letterSpacing: '1px', whiteSpace: 'nowrap' }}>
            <Box component='span' sx={{ color: onColor }}>{'×'.repeat(filled)}</Box>
            <Box component='span' sx={{ color: colors.pale }}>{'×'.repeat(10 - filled)}</Box>
        </Box>
    );
};

export default Stitch;
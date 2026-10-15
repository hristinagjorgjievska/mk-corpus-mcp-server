import { Box, Typography } from '@mui/material';
import { useNavigate } from 'react-router';
import type { SearchRunResponse, SearchStatus } from '../../../../api/types/searchRun.ts';
import { colors, fonts } from '../../../../theme/theme.ts';

interface SearchRunCardProps {
    searchRun: SearchRunResponse;
}

const statusLabel: Record<SearchStatus, string> = {
    PENDING: 'ЧЕКА',
    RUNNING: 'ВО ТЕК',
    COMPLETED: 'ЗАВРШЕНО',
    FAILED: 'НЕУСПЕШНО'
};

const SearchRunCard = ({ searchRun }: SearchRunCardProps) => {
    const navigate = useNavigate();
    const failed = searchRun.status === 'FAILED';

    return (
        <Box
            onClick={() => navigate(`/search-runs/${searchRun.id}`)}
            sx={{
                aspectRatio: '1 / 1', overflow: 'hidden', cursor: 'pointer',
                display: 'flex', flexDirection: 'column', justifyContent: 'space-between',
                p: 2,
                backgroundColor: colors.linen,
                '&:hover': { backgroundColor: colors.pale }
            }}
        >
            <Box>
                <Typography sx={{ fontSize: 11, fontWeight: 800, letterSpacing: '0.1em', color: failed ? colors.muted : colors.red }}>
                    {statusLabel[searchRun.status]}
                </Typography>
                <Typography sx={{ mt: 1, fontFamily: fonts.display, fontWeight: 700, fontSize: 18, lineHeight: 1.2 }}>
                    „{searchRun.query}“
                </Typography>
            </Box>

            <Box>
                <Typography sx={{ fontFamily: fonts.display, fontWeight: 900, fontSize: 28, lineHeight: 1, color: colors.red }}>
                    {searchRun.resultCount ?? 0}
                </Typography>
                <Typography sx={{ fontSize: 11, fontWeight: 800, letterSpacing: '0.08em', color: colors.muted }}>
                    ЗАЧУВАНИ СТАТИИ
                </Typography>
                <Typography sx={{ mt: 1, fontSize: 13, color: colors.muted }}>
                    {new Date(searchRun.createdAt).toLocaleString('mk-MK')}
                </Typography>
            </Box>
        </Box>
    );
};

export default SearchRunCard;
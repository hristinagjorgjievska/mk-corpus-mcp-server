import { Box, Button, InputBase, Typography } from '@mui/material';
import { useState } from 'react';
import { useNavigate } from 'react-router';
import useStats from '../../../../hooks/useStats.ts';
import useAuth from '../../../../hooks/useAuth.ts';
import useSnackbar from '../../../../hooks/useSnackbar.ts';
import searchRunApi from '../../../../api/searchRunApi.ts';
import { colors, fonts } from '../../../../theme/theme.ts';

const HomePage = () => {
    const { stats } = useStats();
    const { isLoggedIn } = useAuth();
    const { showSnackbar } = useSnackbar();
    const navigate = useNavigate();

    const [query, setQuery] = useState('');
    const [searching, setSearching] = useState(false);

    const onSearch = async () => {
        if (!query.trim()) return;
        if (!isLoggedIn) {
            navigate('/login');
            return;
        }

        setSearching(true);
        try {
            const response = await searchRunApi.run({ query: query.trim(), limit: 10 });
            navigate(`/search-runs/${response.data.id}`);
        } catch (err) {
            showSnackbar(err instanceof Error ? err.message : 'Пребарувањето не успеа.', 'error');
        } finally {
            setSearching(false);
        }
    };

    return (
        <Box>
            <Box sx={{ py: 5 }}>
                <Typography sx={{ fontFamily: fonts.display, fontWeight: 900, fontSize: { xs: 40, md: 64 }, lineHeight: 1.02 }}>
                    ДИГИТАЛНА АРХИВА<br/>
                    НА МАКЕДОНСКИОТ <Box component='span' sx={{ color: colors.red }}>ЗБОР.</Box>
                </Typography>

                <Typography sx={{ fontSize: 20, color: colors.muted, mt: 2 }}>
                    {stats
                        ? `Досега: ${stats.searchRuns} пребарувања, ${stats.resources} собрани, ${stats.analyzedResources} прочитани, ${stats.donationBatches} пакети, ${stats.donatedResources} предадени на Везилка.`
                        : 'Македонски текстови, подготвени за Везилка.'}
                </Typography>
            </Box>

            <Box sx={{ display: 'flex' }}>
                <InputBase
                    value={query}
                    onChange={(e) => setQuery(e.target.value)}
                    onKeyDown={(e) => e.key === 'Enter' && onSearch()}
                    placeholder='образование'
                    sx={{
                        flex: 1, backgroundColor: colors.linen, px: 3, py: 2,
                        fontFamily: fonts.display, fontWeight: 700, fontSize: { xs: 20, md: 28 }
                    }}
                />
                <Button
                    onClick={onSearch}
                    disabled={searching}
                    variant='contained'
                    sx={{ px: 4, fontSize: 16 }}
                >
                    {searching ? 'ПРЕБАРУВАМ…' : 'ПРЕБАРАЈ →'}
                </Button>
            </Box>

            {searching && (
                <Typography sx={{ color: colors.muted, mt: 1 }}>
                    Статиите се преземаат...
                </Typography>
            )}
        </Box>
    );
};

export default HomePage;

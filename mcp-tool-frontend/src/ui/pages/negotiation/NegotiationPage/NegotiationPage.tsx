import { Box, Button, CircularProgress, Typography } from '@mui/material';
import useNegotiation from '../../../../hooks/useNegotiation.ts';
import { colors, fonts } from '../../../../theme/theme.ts';

const NegotiationPage = () => {
    const { messages, loading, onRun } = useNegotiation();

    const needs = [...messages].reverse().find((m) => m.needs)?.needs ?? [];
    const sortedNeeds = [...needs].sort((a, b) => b.target - a.target);

    return (
        <Box>
            <Box sx={{ display: 'flex', alignItems: 'baseline', justifyContent: 'space-between', mb: 2, flexWrap: 'wrap', gap: 2 }}>
                <Box>
                    <Typography sx={{ fontFamily: fonts.display, fontWeight: 900, fontSize: 32 }}>ШТО НЕДОСТАСУВА ВО КОРПУСОТ?</Typography>
                    <Typography sx={{ color: colors.muted }}>
                        Агентот бара кои теми недостасуваат во корпусот, ги пребарува на изворот и прави пакет за одобрување.
                    </Typography>
                </Box>
                <Button variant='contained' color='secondary' onClick={onRun} disabled={loading}>
                    {loading ? 'СЕ АНАЛИЗИРА…' : 'ЗАПОЧНИ'}
                </Button>
            </Box>

            {loading && (
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, mt: 4 }}>
                    <CircularProgress size={24}/>
                    <Typography sx={{ color: colors.muted }}>Агентот пребарува на изворот…</Typography>
                </Box>
            )}

            {messages.length > 0 && (
                <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', md: '240px 1fr' }, gap: 3 }}>
                    <Box sx={{ backgroundColor: colors.linen, p: 2, alignSelf: 'start' }}>
                        <Typography sx={{ fontSize: 11, fontWeight: 800, letterSpacing: '0.14em', color: colors.red, mb: 1.5 }}>
                            ПОТРЕБИ НА КОРПУСОР
                        </Typography>
                        {sortedNeeds.map((need) => (
                            <Box key={need.category} sx={{ mb: 1.5 }}>
                                <Box sx={{ display: 'flex', justifyContent: 'space-between', fontSize: 13 }}>
                                    <span>{need.category}</span>
                                    <span style={{ color: colors.muted }}>
                                        {need.received}/{need.target} · {Math.round((need.received / need.target) * 100)}%
                                    </span>
                                </Box>
                                <Box sx={{ height: 6, backgroundColor: colors.pale }}>
                                    <Box sx={{
                                        height: 6, backgroundColor: colors.red, transition: 'width 0.5s',
                                        width: `${Math.min(100, (need.received / need.target) * 100)}%`
                                    }}/>
                                </Box>
                            </Box>
                        ))}
                    </Box>

                    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
                        {messages.map((message, index) => {
                            const isAgent = message.from === 'agent';
                            return (
                                <Box key={index} sx={{
                                    alignSelf: isAgent ? 'flex-end' : 'flex-start',
                                    maxWidth: '80%', px: 2, py: 1,
                                    backgroundColor: isAgent ? colors.brown : colors.linen,
                                    color: isAgent ? colors.cream : colors.brown
                                }}>
                                    <Typography sx={{ fontSize: 10, fontWeight: 800, letterSpacing: '0.14em',
                                        color: isAgent ? colors.blush : colors.red }}>
                                        {isAgent ? 'АГЕНТ' : 'КОРПУС'}
                                    </Typography>
                                    <Typography sx={{ fontSize: 14 }}>{message.text}</Typography>
                                </Box>
                            );
                        })}
                    </Box>
                </Box>
            )}
        </Box>
    );
};

export default NegotiationPage;
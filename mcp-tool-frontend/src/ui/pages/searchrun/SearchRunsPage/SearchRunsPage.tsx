import { Box, Button, CircularProgress, Typography } from '@mui/material';
import { useState } from 'react';
import useSearchRuns from '../../../../hooks/useSearchRuns.ts';
import SearchRunCard from '../../../components/searchrun/SearchRunCard/SearchRunCard.tsx';
import RunSearchDialog from '../../../components/searchrun/RunSearchDialog/RunSearchDialog.tsx';
import { colors, fonts } from '../../../../theme/theme.ts';


/**
 * The search control panel — the fully-worked reference feature. The data flow
 * (useSearchRuns -> SearchRunCard) and the "Run search" dialog are provided as
 * the pattern to mirror for the Resources and Donations features.
 */
const SearchRunsPage = () => {
    const { searchRuns, loading } = useSearchRuns();
    const [runDialogOpen, setRunDialogOpen] = useState<boolean>(false);

    return (
        <Box>
            <Box sx={{ display: 'flex', alignItems: 'baseline', justifyContent: 'space-between', mb: 2, flexWrap: 'wrap', gap: 2 }}>
                <Box>
                    <Typography sx={{ fontFamily: fonts.display, fontWeight: 900, fontSize: 32 }}>ПРЕБАРУВАЊА</Typography>
                    <Typography sx={{ color: colors.muted }}>
                        Внесуваш збор, го пребаруваме изворот и ги чуваме статиите на македонски.
                    </Typography>
                </Box>
                <Button variant='contained' color='secondary' onClick={() => setRunDialogOpen(true)}>+ НОВО ПРЕБАРУВАЊЕ</Button>
            </Box>

            {loading && <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}><CircularProgress/></Box>}

            {!loading && searchRuns.length === 0 && (
                <Typography sx={{color: colors.muted, mt: 3 }}>
                    Уште нема пребарувања. Внеси збор и собери ги првите статии.
                </Typography>
            )}

            {!loading && (
                <Box sx={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(180px, 1fr))', gap: 1.5 }}>
                    {searchRuns.map((searchRun) => (
                        <SearchRunCard key={searchRun.id} searchRun={searchRun}/>
                    ))}
                </Box>
            )}

            <RunSearchDialog open={runDialogOpen} onClose={() => setRunDialogOpen(false)}/>
        </Box>
    );
};

export default SearchRunsPage;
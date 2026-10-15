/**
 * The donation dashboard towards doniraj.vezilka.ai.
 */
import { Box, Button, CircularProgress, Typography } from '@mui/material';
import { useState } from 'react';
import useDonations from '../../../../hooks/useDonations.ts';
import DonationBatchCard from '../../../components/donation/DonationBatchCard/DonationBatchCard.tsx';
import SubmitDonationDialog from '../../../components/donation/SubmitDonationDialog/SubmitDonationDialog.tsx';
import { colors, fonts } from '../../../../theme/theme.ts';

const DonationsPage = () => {
    const { donations, loading, onCreate, onApprove, onSubmit } = useDonations();
    const [dialogOpen, setDialogOpen] = useState(false);

    const texts = donations.reduce((sum, b) => sum + b.resourceCount, 0);
    const accepted = donations.filter((b) => b.status === 'ACCEPTED').length;

    return (
        <Box>
            <Box sx={{ display: 'flex', alignItems: 'baseline', justifyContent: 'space-between', mb: 2, flexWrap: 'wrap', gap: 2 }}>
                <Box>
                    <Typography sx={{ fontFamily: fonts.display, fontWeight: 900, fontSize: 32 }}>ДОНАЦИИ</Typography>
                    <Typography sx={{ color: colors.muted }}>
                        {donations.length} пакети · {texts} текстови · {accepted} прифатени од Везилка
                    </Typography>
                </Box>
                <Button variant='contained' color='secondary' onClick={() => setDialogOpen(true)}>+ НОВ ПАКЕТ</Button>
            </Box>

            <Box sx={{ display: 'flex', gap: 3, flexWrap: 'wrap', backgroundColor: colors.linen, px: 2, py: 1, mb: 0.5,
                fontSize: 11, fontWeight: 800, letterSpacing: '0.1em', color: colors.muted }}>
                <span>■ ДРАФТ</span><span>■■ ОДОБРЕНО</span><span>■■■ ИСПРАТЕНО</span>
                <Box component='span' sx={{ color: colors.red }}>■■■■ ПРИФАТЕНО</Box>
            </Box>

            {loading && <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}><CircularProgress/></Box>}

            {!loading && donations.length === 0 && (
                <Typography sx={{ color: colors.muted, mt: 3 }}>
                    Се уште нема пакети. Прочитај неколку статии и направи нов пакет.
                </Typography>
            )}

            {!loading && donations.map((batch, index) => (
                <DonationBatchCard key={batch.id} batch={batch} index={index} onApprove={onApprove} onSubmit={onSubmit}/>
            ))}

            <SubmitDonationDialog open={dialogOpen} onClose={() => setDialogOpen(false)} onCreate={onCreate}/>
        </Box>
    );
};

export default DonationsPage;

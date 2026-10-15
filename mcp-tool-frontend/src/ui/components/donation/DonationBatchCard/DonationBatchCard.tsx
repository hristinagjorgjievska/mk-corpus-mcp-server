import { Box, Button, Typography } from '@mui/material';
import type { DonationBatchResponse, DonationStatus } from '../../../../api/types/donation.ts';
import { colors, fonts } from '../../../../theme/theme.ts';

interface DonationBatchCardProps {
    batch: DonationBatchResponse;
    index: number;
    onApprove: (id: number) => void;
    onSubmit: (id: number) => void;
}

const filled: Record<DonationStatus, number> = {
    DRAFT: 1, APPROVED: 2, SUBMITTED: 3, ACCEPTED: 4, REJECTED: 4, FAILED: 4
};

const label: Record<DonationStatus, string> = {
    DRAFT: 'чека одобрување',
    APPROVED: 'одобрено, чека испраќање',
    SUBMITTED: 'испратено, Везилка проверува',
    ACCEPTED: 'прифатено во корпусот',
    REJECTED: 'одбиено од Везилка',
    FAILED: 'испраќањето не успеа'
};

const DonationBatchCard = ({ batch, index, onApprove, onSubmit }: DonationBatchCardProps) => {
    const failed = batch.status === 'REJECTED' || batch.status === 'FAILED';
    const squareColor = (i: number) =>
        i >= filled[batch.status] ? colors.pale : failed && i === 3 ? colors.brown : colors.red;

    return (
        <Box sx={{
            display: 'grid',
            gridTemplateColumns: { xs: '50px 1fr', md: '60px 1fr 140px 170px' },
            alignItems: 'center', gap: 2, px: 2, py: 1.5,
            backgroundColor: index % 2 === 1 ? colors.linen : 'transparent'
        }}>
            <Typography sx={{ fontFamily: fonts.display, fontWeight: 900, fontSize: 18, color: colors.red }}>
                #{batch.id}
            </Typography>

            <Typography sx={{ fontWeight: 800, fontSize: 13 }}>
                {batch.resourceCount} {batch.resourceCount === 1 ? 'ТЕКСТ' : 'ТЕКСТА'} ·{' '}
                <Box component='span' sx={{ fontWeight: 400, color: failed ? colors.red : colors.muted }}>
                    {label[batch.status]}
                </Box>
            </Typography>

            <Box sx={{ display: 'flex', gap: 0.5 }} title={batch.status}>
                {[0, 1, 2, 3].map((i) => (
                    <Box key={i} sx={{ width: 28, height: 28, backgroundColor: squareColor(i) }}/>
                ))}
            </Box>

            <Box sx={{ textAlign: { md: 'right' } }}>
                {batch.status === 'DRAFT' && (
                    <Button variant='contained' color='secondary' fullWidth onClick={() => onApprove(batch.id)}>ОДОБРИ →</Button>
                )}
                {batch.status === 'APPROVED' && (
                    <Button variant='contained' fullWidth onClick={() => onSubmit(batch.id)}>ИСПРАТИ →</Button>
                )}
                {batch.vezilkaReference && (
                    <Typography sx={{ fontSize: 11, fontWeight: 800, letterSpacing: '0.08em', color: colors.red }}>
                        REF {batch.vezilkaReference.toUpperCase()}
                    </Typography>
                )}
            </Box>
        </Box>
    );
};

export default DonationBatchCard;

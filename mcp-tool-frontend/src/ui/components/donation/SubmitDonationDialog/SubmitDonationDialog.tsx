import { Box, Button, Checkbox, Dialog, DialogActions, DialogContent, DialogTitle, Typography } from '@mui/material';
import { useEffect, useState } from 'react';
import resourceApi from '../../../../api/resourceApi.ts';
import type { ResourceResponse } from '../../../../api/types/resource.ts';
import type { CreateDonationBatchRequest } from '../../../../api/types/donation.ts';
import Stitch from '../../resource/Stitch/Stitch.tsx';
import { colors, fonts } from '../../../../theme/theme.ts';

interface SubmitDonationDialogProps {
    open: boolean;
    onClose: () => void;
    onCreate: (data: CreateDonationBatchRequest) => Promise<void>;
}

const SubmitDonationDialog = ({ open, onClose, onCreate }: SubmitDonationDialogProps) => {
    const [candidates, setCandidates] = useState<ResourceResponse[]>([]);
    const [selected, setSelected] = useState<number[]>([]);

    useEffect(() => {
        if (!open) return;
        setSelected([]);
        resourceApi.findAll({ analyzed: true, donated: false }, 0, 50)
            .then((response) => setCandidates(response.data.content))
            .catch(() => setCandidates([]));
    }, [open]);

    const toggle = (id: number) =>
        setSelected((prev) => (prev.includes(id) ? prev.filter((x) => x !== id) : [...prev, id]));

    const onConfirm = async () => {
        await onCreate({ resourceIds: selected });
        onClose();
    };

    return (
        <Dialog open={open} onClose={onClose} fullWidth maxWidth='md'>
            <DialogTitle sx={{ fontFamily: fonts.display, fontWeight: 900 }}>НОВ ПАКЕТ</DialogTitle>

            <DialogContent>
                <Typography sx={{ color: colors.muted, mb: 2 }}>
                    Избери прочитани редови. До Везилка ќе замине до 30% од секој текст, со извор и линк до оригиналот.
                </Typography>

                {candidates.length === 0 && (
                    <Typography sx={{ fontFamily: fonts.voice, fontStyle: 'italic' }}>
                        Нема прочитани редови што чекаат. Прочитај некој во „Редови“.
                    </Typography>
                )}

                {candidates.map((resource, index) => (
                    <Box key={resource.id}
                         onClick={() => toggle(resource.id)}
                         sx={{
                             display: 'grid', gridTemplateColumns: 'auto 1fr auto', gap: 1, alignItems: 'start',
                             p: 1.5, cursor: 'pointer',
                             backgroundColor: selected.includes(resource.id) ? '#F4DAD5' : index % 2 ? colors.linen : 'transparent'
                         }}>
                        <Checkbox checked={selected.includes(resource.id)} size='small' sx={{ p: 0, mt: 0.3 }}/>
                        <Box sx={{ minWidth: 0 }}>
                            <Typography sx={{ fontFamily: fonts.display, fontWeight: 700, fontSize: 13 }}>{resource.title}</Typography>
                            <Typography sx={{ fontSize: 12, color: colors.muted, mt: 0.5 }}>
                                {(resource.content ?? '').slice(0, 180)}…
                            </Typography>
                        </Box>
                        <Stitch value={resource.macedonianConfidence}/>
                    </Box>
                ))}
            </DialogContent>

            <DialogActions sx={{ p: 2 }}>
                <Button onClick={onClose} sx={{ color: colors.muted }}>ОТКАЖИ</Button>
                <Button variant='contained' disabled={selected.length === 0} onClick={onConfirm}>
                    ДОДАЈ ({selected.length}) →
                </Button>
            </DialogActions>
        </Dialog>
    );
};

export default SubmitDonationDialog;

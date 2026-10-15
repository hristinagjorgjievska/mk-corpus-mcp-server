import { Box, Typography } from '@mui/material';
import { useNavigate } from 'react-router';
import type { ResourceResponse } from '../../../../api/types/resource.ts';
import { colors, fonts } from '../../../../theme/theme.ts';

interface ResourceCardProps {
    resource: ResourceResponse;
    index: number;
    onAnalyze: (id: number) => void;
    onDelete: (id: number) => void;
}

const ResourceCard = ({ resource, index, onAnalyze, onDelete }: ResourceCardProps) => {
    const navigate = useNavigate();

    const confidence = resource.macedonianConfidence ?? 0;
    const lowMk = confidence < 0.5;
    const donated = resource.donationBatchId !== null;
    const analyzed = resource.analysis !== null;

    const action = { fontSize: 11, fontWeight: 800, letterSpacing: '0.1em', cursor: 'pointer', color: colors.muted, '&:hover': { color: colors.red } };

    return (
        <Box sx={{
            display: 'grid', gridTemplateColumns: '40px 1fr auto', alignItems: 'center', gap: 2,
            px: 2, py: 1.5,
            backgroundColor: index % 2 === 1 ? colors.linen : 'transparent',
            color: lowMk ? colors.pale : colors.brown
        }}>
            <Typography sx={{ fontFamily: fonts.display, fontWeight: 900, fontSize: 14, color: lowMk ? colors.pale : colors.red }}>
                {String(index + 1).padStart(2, '0')}
            </Typography>

            <Box sx={{ minWidth: 0 }}>
                <Typography
                    onClick={() => navigate(`/resources/${resource.id}`)}
                    sx={{
                        fontFamily: fonts.display, fontWeight: 700, fontSize: 14, lineHeight: 1.3, cursor: 'pointer',
                        textDecoration: lowMk ? 'line-through' : 'none',
                        '&:hover': { color: colors.red }
                    }}
                >
                    {resource.title ?? `Ред #${resource.id}`}
                </Typography>

                <Box sx={{ display: 'flex', gap: 2, mt: 0.5, flexWrap: 'wrap', fontSize: 11, fontWeight: 800, letterSpacing: '0.08em', color: colors.muted }}>
                    <span>{resource.wordCount ?? 0} ЗБОРОВИ</span>
                    {analyzed && <Box component='span' sx={{ color: colors.red }}>✎ ПРОЧИТАНО</Box>}
                    {donated && <Box component='span' sx={{ color: colors.red }}>ДОНИРАНО #{resource.donationBatchId}</Box>}
                    {resource.sourceUrl && (
                        <Box component='a' href={resource.sourceUrl} target='_blank' rel='noopener' sx={{ color: colors.muted }}>
                            ИЗВОР ↗
                        </Box>
                    )}
                    {!analyzed && <Box component='span' sx={action} onClick={() => onAnalyze(resource.id)}>ПРОЧИТАЈ</Box>}
                    {!donated && <Box component='span' sx={action} onClick={() => onDelete(resource.id)}>ИЗБРИШИ</Box>}
                </Box>
            </Box>

            <Box sx={{ textAlign: 'right' }}>
                <Typography sx={{ fontSize: 10, fontWeight: 800, letterSpacing: '0.1em', color: colors.muted }}>
                    МАКЕДОНСКИ
                </Typography>
                <Typography sx={{ fontWeight: 800, fontSize: 16, color: lowMk ? colors.pale : colors.red }}>
                    {confidence.toFixed(1)}
                </Typography>
            </Box>
        </Box>
    );
};

export default ResourceCard;
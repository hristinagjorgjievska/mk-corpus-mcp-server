import { Box, CircularProgress, Typography } from '@mui/material';
import { useCallback, useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router';
import resourceApi from '../../../../api/resourceApi.ts';
import donationApi from '../../../../api/donationApi.ts';
import type { ResourceResponse } from '../../../../api/types/resource.ts';
import useSnackbar from '../../../../hooks/useSnackbar.ts';
import ResourceAnalysisPanel from '../../../components/resource/ResourceAnalysisPanel/ResourceAnalysisPanel.tsx';
import Stitch from '../../../components/resource/Stitch/Stitch.tsx';
import { colors, fonts } from '../../../../theme/theme.ts';

const ResourceDetailsPage = () => {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();
    const { showSnackbar } = useSnackbar();

    const [resource, setResource] = useState<ResourceResponse | null>(null);
    const [loading, setLoading] = useState(true);
    const [analyzing, setAnalyzing] = useState(false);

    const load = useCallback(async () => {
        if (!id) return;
        setLoading(true);
        try {
            const response = await resourceApi.findById(id);
            setResource(response.data);
        } catch (err) {
            showSnackbar(err instanceof Error ? err.message : 'Редот не може да се вчита.', 'error');
        } finally {
            setLoading(false);
        }
    }, [id, showSnackbar]);

    useEffect(() => {
        void load();
    }, [load]);

    const onAnalyze = async () => {
        if (!id) return;
        setAnalyzing(true);
        try {
            const response = await resourceApi.analyze(id);
            setResource(response.data);
            showSnackbar('Редот е прочитан.', 'success');
        } catch (err) {
            showSnackbar(err instanceof Error ? err.message : 'Анализата не успеа.', 'error');
        } finally {
            setAnalyzing(false);
        }
    };

    const onDonate = async () => {
        if (!resource) return;
        try {
            await donationApi.add({ resourceIds: [resource.id] });
            showSnackbar('Статијата е додадена во нов пакет.', 'success');
            navigate('/donations');
        } catch (err) {
            showSnackbar(err instanceof Error ? err.message : 'Пакетот не може да се креира.', 'error');
        }
    };

    if (loading) {
        return <Box sx={{ display: 'flex', justifyContent: 'center', mt: 6 }}><CircularProgress/></Box>;
    }

    if (!resource) {
        return (
            <Typography sx={{ color: colors.muted }}>
                Оваа статија не постои во тетратката.
            </Typography>
        );
    }

    const small = { fontSize: 11, fontWeight: 800, letterSpacing: '0.12em', color: colors.muted };

    return (
        <Box>
            <Link to='/resources'>
                <Typography sx={{ ...small, mb: 2, '&:hover': { color: colors.red } }}>‹ НАЗАД КОН СТАТИИ</Typography>
            </Link>

            <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', md: 'minmax(0, 1.6fr) minmax(0, 1fr)' } }}>
                <Box sx={{ backgroundColor: colors.cream, pr: { md: 4 }, pb: 4 }}>
                    <Typography sx={small}>
                        РЕД {String(resource.id).padStart(2, '0')} ·{resource.wordCount ?? 0} ЗБОРОВИ
                        {resource.donationBatchId !== null && ` · ВО ПАКЕТ #${resource.donationBatchId}`}
                    </Typography>

                    <Typography sx={{ fontFamily: fonts.display, fontWeight: 900, fontSize: { xs: 24, md: 32 }, lineHeight: 1.15, my: 2 }}>
                        {resource.title ?? `Ред #${resource.id}`}
                    </Typography>

                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, mb: 3 }}>
                        <Stitch value={resource.macedonianConfidence}/>
                        <Typography sx={small}>МК {(resource.macedonianConfidence ?? 0).toFixed(2)}</Typography>
                        {resource.sourceUrl && (
                            <Box component='a' href={resource.sourceUrl} target='_blank' rel='noopener'
                                 sx={{ ...small, color: colors.red }}>
                                ОРИГИНАЛ ↗
                            </Box>
                        )}
                    </Box>

                    <Typography sx={{ fontSize: 16, lineHeight: 1.8, whiteSpace: 'pre-line' }}>
                        {resource.content}
                    </Typography>
                </Box>

                <ResourceAnalysisPanel
                    analysis={resource.analysis}
                    resourceId={resource.id}
                    wordCount={resource.wordCount}
                    analyzing={analyzing}
                    onAnalyze={onAnalyze}
                    canDonate={resource.analysis !== null && resource.donationBatchId === null}
                    onDonate={onDonate}
                />
            </Box>
        </Box>
    );
};

export default ResourceDetailsPage;

import { Box, Button, Typography } from '@mui/material';
import type { ResourceAnalysisResponse } from '../../../../api/types/resource.ts';
import { colors, fonts } from '../../../../theme/theme.ts';

interface ResourceAnalysisPanelProps {
    analysis: ResourceAnalysisResponse | null;
    resourceId: number;
    wordCount: number | null;
    analyzing: boolean;
    onAnalyze: () => void;
    canDonate: boolean;
    onDonate: () => void;
}


const ResourceAnalysisPanel = ({ analysis, resourceId, wordCount, analyzing, onAnalyze, canDonate, onDonate }: ResourceAnalysisPanelProps) => {
    const small = { fontSize: 11, fontWeight: 800, letterSpacing: '0.14em', color: colors.blush };

    return (
        <Box sx={{ backgroundColor: colors.brown, color: colors.cream, p: 3, height: '100%' }}>
            <Typography sx={{ ...small, mb: 2 }}>
                НА МАРГИНА · РЕД {String(resourceId).padStart(2, '0')}
            </Typography>

            {!analysis && (
                <>
                    <Typography sx={{fontSize: 16, lineHeight: 1.5, mb: 3 }}>
                        Оваа статија се уште не е прочитана. Нема резиме ни клучни зборови.
                    </Typography>
                    <Button variant='contained' onClick={onAnalyze} disabled={analyzing}>
                        {analyzing ? 'ЧИТАМ…' : 'ПРОЧИТАЈ →'}
                    </Button>
                </>
            )}

            {analysis && (
                <>
                    {analysis.summary && (
                        <Typography sx={{ fontSize: 16, lineHeight: 1.5, mb: 3 }}>
                            {analysis.summary}
                        </Typography>
                    )}

                    {analysis.keywords && (
                        <Typography sx={{ fontFamily: fonts.display, fontWeight: 900, fontSize: 18, lineHeight: 1.35, color: colors.blush, mb: 3 }}>
                            {analysis.keywords.split(',').map((k) => k.trim().toUpperCase()).join(' · ')}
                        </Typography>
                    )}

                    <Typography sx={{ fontSize: 11, fontWeight: 800, letterSpacing: '0.1em', mb: 3 }}>
                        {analysis.sentenceCount ?? 0} РЕЧЕНИЦИ · {wordCount ?? 0} ЗБОРОВИ · БОД {(analysis.macedonianConfidence ?? 0).toFixed(2)}
                    </Typography>

                    <Box sx={{ display: 'flex', gap: 1, flexWrap: 'wrap' }}>
                        {canDonate && (
                            <Button variant='contained' onClick={onDonate}>ДОДАЈ ВО ПАКЕТ →</Button>
                        )}
                        <Button onClick={onAnalyze} disabled={analyzing} sx={{ color: colors.cream }}>
                            {analyzing ? 'ЧИТАМ…' : 'ПРОЧИТАЈ ПАК'}
                        </Button>
                    </Box>
                </>
            )}
        </Box>
    );
};

export default ResourceAnalysisPanel;

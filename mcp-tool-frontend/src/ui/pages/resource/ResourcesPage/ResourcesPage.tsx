import { Box, CircularProgress, Pagination, Typography } from '@mui/material';
import { useState } from 'react';
import type { ResourceFilter } from '../../../../api/types/resource.ts';
import useResources from '../../../../hooks/useResources.ts';
import ResourceFilters from '../../../components/resource/ResourceFilters/ResourceFilters.tsx';
import ResourceGrid from '../../../components/resource/ResourceGrid/ResourceGrid.tsx';
import { colors, fonts } from '../../../../theme/theme.ts';

const PAGE_SIZE = 12;

/**
 * The collected-resource browser.
 */
const ResourcesPage = () => {
    const [filter, setFilter] = useState<ResourceFilter>({});
    const [page, setPage] = useState<number>(0);

    const { resources, loading, onAnalyze, onDelete } = useResources(filter, page, PAGE_SIZE);

    const onFilterChange = (next: ResourceFilter) => {
        setFilter(next);
        setPage(0);
    };

    return (
        <Box>
            <Box sx={{ display: 'flex', alignItems: 'baseline', justifyContent: 'space-between', mb: 2 }}>
                <Typography sx={{ fontFamily: fonts.display, fontWeight: 900, fontSize: 32 }}>СТАТИИ</Typography>
                {resources && (
                    <Typography sx={{ color: colors.muted }}>
                        {resources.totalElements} запишани
                    </Typography>
                )}
            </Box>

            <ResourceFilters filter={filter} onChange={onFilterChange}/>

            {loading && (
                <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
                    <CircularProgress/>
                </Box>
            )}

            {!loading && (!resources || resources.content.length === 0) && (
                <Typography sx={{ color: colors.muted, mt: 3 }}>
                    Тетратката е празна. Пребарај нешто на почетната страница.
                </Typography>
            )}

            {!loading && resources && resources.content.length > 0 && (
                <>
                    <ResourceGrid resources={resources.content} onAnalyze={onAnalyze} onDelete={onDelete}/>

                    {resources.totalPages > 1 && (
                        <Box sx={{ display: 'flex', justifyContent: 'center', mt: 3 }}>
                            <Pagination
                                count={resources.totalPages}
                                page={page + 1}
                                onChange={(_, p) => setPage(p - 1)}
                                color='primary'
                                shape='rounded'
                            />
                        </Box>
                    )}
                </>
            )}
        </Box>
    );
};

export default ResourcesPage;

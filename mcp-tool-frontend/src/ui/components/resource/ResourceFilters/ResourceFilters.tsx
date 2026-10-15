import { Box, InputBase, MenuItem, Select, Slider } from '@mui/material';
import { useEffect, useState } from 'react';
import type { ResourceFilter } from '../../../../api/types/resource.ts';
import type { SearchRunResponse } from '../../../../api/types/searchRun.ts';
import searchRunApi from '../../../../api/searchRunApi.ts';
import { colors } from '../../../../theme/theme.ts';

interface ResourceFiltersProps {
    filter: ResourceFilter;
    onChange: (filter: ResourceFilter) => void;
}

const toTri = (v: boolean | undefined) => (v === undefined ? 'all' : v ? 'yes' : 'no');
const fromTri = (v: string) => (v === 'all' ? undefined : v === 'yes');

const ResourceFilters = ({ filter, onChange }: ResourceFiltersProps) => {
    const [searchRuns, setSearchRuns] = useState<SearchRunResponse[]>([]);
    const [text, setText] = useState(filter.search ?? '');
    const [confidence, setConfidence] = useState(filter.minMacedonianConfidence ?? 0);

    useEffect(() => {
        searchRunApi.findAll().then((response) => setSearchRuns(response.data)).catch(() => {});
    }, []);

    const set = (patch: Partial<ResourceFilter>) => onChange({ ...filter, ...patch });

    const label = { fontSize: 11, fontWeight: 800, letterSpacing: '0.1em', color: colors.muted, mr: 1 };
    const select = { fontSize: 12, fontWeight: 800, '& .MuiOutlinedInput-notchedOutline': { border: 'none' } };

    return (
        <Box sx={{ backgroundColor: colors.linen, px: 2, py: 1, mb: 0.5, display: 'flex', flexWrap: 'wrap', alignItems: 'center', gap: 3 }}>
            <InputBase
                value={text}
                onChange={(e) => setText(e.target.value)}
                onKeyDown={(e) => e.key === 'Enter' && set({ search: text.trim() || undefined })}
                placeholder='ПРЕБАРАЈ СПОРЕД ЗБОР ↵'
                sx={{ fontSize: 12, fontWeight: 800, minWidth: 160 }}
            />

            <Box sx={{ display: 'flex', alignItems: 'center' }}>
                <Box component='span' sx={label}>МАКЕДОНСКИ ≥ {confidence.toFixed(1)}</Box>
                <Slider size='small' min={0} max={1} step={0.1} value={confidence} sx={{ width: 120 }}
                        onChange={(_, v) => setConfidence(v as number)}
                        onChangeCommitted={(_, v) => set({ minMacedonianConfidence: (v as number) || undefined })}/>
            </Box>

            <Box>
                <Box component='span' sx={label}>ПРЕБАРУВАЊЕ</Box>
                <Select size='small' value={filter.searchRunId ?? ''} displayEmpty sx={select}
                        onChange={(e) => set({ searchRunId: String(e.target.value) === '' ? undefined : Number(e.target.value) })}>
                    <MenuItem value=''>СИТЕ</MenuItem>
                    {searchRuns.map((run) => (
                        <MenuItem key={run.id} value={run.id}>#{run.id} {run.query.toUpperCase()}</MenuItem>
                    ))}
                </Select>
            </Box>

            <Box>
                <Box component='span' sx={label}>ПРОЧИТАНИ</Box>
                <Select size='small' value={toTri(filter.analyzed)} sx={select}
                        onChange={(e) => set({ analyzed: fromTri(e.target.value) })}>
                    <MenuItem value='all'>СИТЕ</MenuItem>
                    <MenuItem value='yes'>ДА</MenuItem>
                    <MenuItem value='no'>НЕ</MenuItem>
                </Select>
            </Box>

            <Box>
                <Box component='span' sx={label}>ДОНИРАНИ</Box>
                <Select size='small' value={toTri(filter.donated)} sx={select}
                        onChange={(e) => set({ donated: fromTri(e.target.value) })}>
                    <MenuItem value='all'>СИТЕ</MenuItem>
                    <MenuItem value='yes'>ДА</MenuItem>
                    <MenuItem value='no'>НЕ</MenuItem>
                </Select>
            </Box>
        </Box>
    );
};

export default ResourceFilters;
import {
    Alert, Box, Button, Chip, CircularProgress, Grid, List, ListItemButton, ListItemText,
    Paper, TextField, Typography
} from '@mui/material';
import { useCallback, useEffect, useMemo, useState } from 'react';
import useMcpTools from '../../../../hooks/useMcpTools.ts';
import type { InvokeToolResponse } from '../../../../api/types/mcp.ts';
import type { ToolInvocationLogResponse } from '../../../../api/types/toolLog.ts';
import toolLogApi from '../../../../api/toolLogApi.ts';
import useSnackbar from '../../../../hooks/useSnackbar.ts';
import ToolLogViewer from '../../../components/searchrun/ToolLogViewer/ToolLogViewer.tsx';
import { colors, fonts } from '../../../../theme/theme.ts';

/**
 * The MCP playground: pick a tool the server exposes, inspect its JSON Schema,
 * provide arguments as JSON and invoke it — exactly what an MCP agent such as
 * the Vezilka host does. Fully provided. The provided `corpus_stats` tool works
 * out of the box; the others report "not implemented" until you build them.
 */

const McpPlaygroundPage = () => {
    const { tools, loading, invoking, invoke } = useMcpTools();
    const { showSnackbar } = useSnackbar();

    const [selected, setSelected] = useState<string | null>(null);
    const [values, setValues] = useState<Record<string, string>>({});
    const [response, setResponse] = useState<InvokeToolResponse | null>(null);
    const [logs, setLogs] = useState<ToolInvocationLogResponse[]>([]);

    const loadLogs = useCallback(async () => {
        try {
            const result = await toolLogApi.findAll();
            setLogs(result.data);
        } catch {
            setLogs([]);
        }
    }, []);

    useEffect(() => {
        void loadLogs();
    }, [loadLogs]);

    const selectedTool = useMemo(() => tools.find((t) => t.name === selected) ?? null, [tools, selected]);

    const prettySchema = useMemo(() => {
        if (!selectedTool) {
            return '';
        }
        try {
            return JSON.stringify(JSON.parse(selectedTool.inputSchema), null, 2);
        } catch {
            return selectedTool.inputSchema;
        }
    }, [selectedTool]);

    const prettyResult = useMemo(() => {
        if (!response?.result) {
            return '';
        }
        try {
            return JSON.stringify(JSON.parse(response.result), null, 2);
        } catch {
            return response.result;
        }
    }, [response]);

    const params = useMemo(() => {
        if (!selectedTool) {
            return [];
        }
        try {
            const schema = JSON.parse(selectedTool.inputSchema);
            const required: string[] = schema.required ?? [];
            return Object.entries(schema.properties ?? {}).map(([name, def]) => {
                const d = def as { type?: string; description?: string };
                return {
                    name,
                    type: d.type ?? 'string',
                    description: d.description ?? '',
                    required: required.includes(name)
                };
            });
        } catch {
            return [];
        }
    }, [selectedTool]);

    const handleSelect = (name: string) => {
        setSelected(name);
        setValues({});
        setResponse(null);
    };

    const handleInvoke = async () => {
        if (!selectedTool) {
            return;
        }

        const parsed: Record<string, unknown> = {};
        for (const p of params) {
            const raw = (values[p.name] ?? '').trim();
            if (raw === '') {
                if (p.required) {
                    showSnackbar(`Полето „${p.name}“ е задолжително.`, 'error');
                    return;
                }
                continue;
            }
            parsed[p.name] = p.type === 'integer' || p.type === 'number' ? Number(raw) : raw;
        }

        const result = await invoke(selectedTool.name, parsed);
        if (result) {
            setResponse(result);
        }
        await loadLogs();
    };

    if (loading) {
        return (
            <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
                <CircularProgress/>
            </Box>
        );
    }

    const small = { fontSize: 11, fontWeight: 800, letterSpacing: '0.12em', color: colors.muted };

    return (
        <Box>
            <Typography sx={{ fontFamily: fonts.display, fontWeight: 900, fontSize: 32 }}>MCP</Typography>
            <Typography sx={{ color: colors.muted, mb: 2 }}>
                Алатките што серверот ги нуди на AI агентот. Избери една за да ја видиш нејзината шема, пополни ги параметрите и повикај ја.
            </Typography>

            <Grid container spacing={2}>
                <Grid size={{ xs: 12, md: 4 }}>
                    <Paper sx={{ backgroundColor: colors.linen }}>
                        <List disablePadding>
                            {tools.map((tool) => (
                                <ListItemButton
                                    key={tool.name}
                                    selected={tool.name === selected}
                                    onClick={() => handleSelect(tool.name)}
                                    sx={{
                                        '&.Mui-selected': { backgroundColor: colors.red, color: colors.cream },
                                        '&.Mui-selected:hover': { backgroundColor: colors.red },
                                        '&.Mui-selected .MuiListItemText-secondary': { color: colors.blush }
                                    }}
                                >
                                    <ListItemText
                                        primary={tool.name}
                                        secondary={tool.description}
                                        slotProps={{
                                            primary: { sx: { a: fonts.display, fontWeight: 700, fontSize: 14 } },
                                            secondary: { sx: { fontSize: 12 } }
                                        }}
                                    />
                                </ListItemButton>
                            ))}
                            {tools.length === 0 && (
                                <Box sx={{ p: 2 }}>
                                    <Typography sx={{color: colors.muted }}>
                                        Нема регистрирани алатки.
                                    </Typography>
                                </Box>
                            )}
                        </List>
                    </Paper>
                </Grid>

                <Grid size={{ xs: 12, md: 8 }}>
                    {selectedTool ? (
                        <Paper sx={{ backgroundColor: colors.linen, p: 2, display: 'flex', flexDirection: 'column', gap: 2 }}>
                            <Box>
                                <Typography sx={{ fontFamily: fonts.display, fontWeight: 900, fontSize: 20 }}>{selectedTool.name}</Typography>
                                <Typography sx={{ fontSize: 13, color: colors.muted }}>{selectedTool.description}</Typography>
                            </Box>

                            <Box>
                                <Typography sx={{ ...small, mb: 1 }}>ШЕМА</Typography>
                                <Box component='pre' sx={{ m: 0, p: 1.5, bgcolor: colors.cream, overflowX: 'auto', fontSize: 12 }}>
                                    {prettySchema}
                                </Box>
                            </Box>

                            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                                <Typography sx={small}>ПАРАМЕТРИ</Typography>
                                {params.length === 0 && (
                                    <Typography sx={{ color: colors.muted }}>
                                        Оваа алатка нема параметри.
                                    </Typography>
                                )}
                                {params.map((p) => (
                                    <TextField
                                        key={p.name}
                                        label={p.required ? `${p.name} *` : p.name}
                                        helperText={p.description}
                                        type={p.type === 'integer' || p.type === 'number' ? 'number' : 'text'}
                                        value={values[p.name] ?? ''}
                                        onChange={(e) => setValues((prev) => ({ ...prev, [p.name]: e.target.value }))}
                                        onKeyDown={(e) => e.key === 'Enter' && handleInvoke()}
                                        fullWidth
                                        sx={{ backgroundColor: colors.cream }}
                                    />
                                ))}
                            </Box>

                            <Box>
                                <Button variant='contained' onClick={handleInvoke} disabled={invoking}>
                                    {invoking ? 'СЕ ИЗВРШУВА…' : 'ПОВИКАЈ →'}
                                </Button>
                            </Box>

                            {response && (
                                <Box>
                                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 1 }}>
                                        <Typography sx={small}>РЕЗУЛТАТ</Typography>
                                        <Chip
                                            label={response.successful ? 'УСПЕШНО' : 'ГРЕШКА'}
                                            color={response.successful ? 'primary' : 'secondary'}
                                            size='small'
                                        />
                                    </Box>
                                    {response.successful ? (
                                        <Box component='pre' sx={{ m: 0, p: 1.5, bgcolor: colors.cream, overflowX: 'auto', fontSize: 12 }}>
                                            {prettyResult}
                                        </Box>
                                    ) : (
                                        <Alert severity='warning'>{response.error}</Alert>
                                    )}
                                </Box>
                            )}
                        </Paper>
                    ) : (
                        <Paper sx={{ backgroundColor: colors.linen, p: 2 }}>
                            <Typography sx={{ color: colors.muted }}>
                                Избери алатка од листата.
                            </Typography>
                        </Paper>
                    )}
                </Grid>
            </Grid>

            <Box sx={{ mt: 4 }}>
                <Typography sx={{ fontFamily: fonts.display, fontWeight: 900, fontSize: 20, mb: 1 }}>ИЗВРШЕНИ ПОВИЦИ</Typography>
                <Typography sx={{ color: colors.muted, mb: 2 }}>
                    Секој повик на алатка се запишува: што е повикано, со кои аргументи и дали е успешно.
                </Typography>
                <ToolLogViewer logs={logs}/>
            </Box>
        </Box>
    );
};

export default McpPlaygroundPage;

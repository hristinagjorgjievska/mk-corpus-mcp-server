import {
  Button, Dialog, DialogActions, DialogContent, DialogTitle, Stack, TextField
} from '@mui/material';
import { useState } from 'react';
import useSearchRuns from '../../../../hooks/useSearchRuns.ts';

interface RunSearchDialogProps {
  open: boolean;
  onClose: () => void;
}

/**
 * The "run a search" form — part of the fully-worked reference slice. It calls
 * useSearchRuns().onRun, which triggers the same work the `search_resources`
 * MCP tool does on the backend.
 */
const RunSearchDialog = ({ open, onClose }: RunSearchDialogProps) => {
  const { onRun } = useSearchRuns();

  const [query, setQuery] = useState<string>('');
  const [limit, setLimit] = useState<string>('10');
  const [submitting, setSubmitting] = useState<boolean>(false);

  const handleClose = () => {
    setQuery('');
    setLimit('10');
    onClose();
  };

  const handleSubmit = async () => {
    if (!query.trim()) {
      return;
    }
    setSubmitting(true);
    try {
      const parsedLimit = Number(limit);
      await onRun({ query: query.trim(), limit: Number.isFinite(parsedLimit) && parsedLimit > 0 ? parsedLimit : undefined });
      handleClose();
    } finally {
      setSubmitting(false);
    }
  };

    return (
        <Dialog open={open} onClose={handleClose} fullWidth maxWidth='sm'>
            <DialogTitle>Ново пребарување</DialogTitle>
            <DialogContent>
                <Stack spacing={2} sx={{ mt: 1 }}>
                    <TextField
                        label='Поим за пребарување'
                        value={query}
                        onChange={(e) => setQuery(e.target.value)}
                        fullWidth
                        autoFocus
                        required
                    />
                    <TextField
                        label='Максимален број резултати'
                        type='number'
                        value={limit}
                        onChange={(e) => setLimit(e.target.value)}
                        fullWidth
                        slotProps={{ htmlInput: { min: 1 } }}
                    />
                </Stack>
            </DialogContent>
            <DialogActions>
                <Button onClick={handleClose}>Откажи</Button>
                <Button variant='contained' onClick={handleSubmit} disabled={!query.trim() || submitting}>
                    Пребарај
                </Button>
            </DialogActions>
        </Dialog>
    );
};

export default RunSearchDialog;

import { Box, Chip, List, ListItem, ListItemText, Typography } from '@mui/material';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import ErrorIcon from '@mui/icons-material/Error';
import type { ToolInvocationLogResponse } from '../../../../api/types/toolLog.ts';

interface ToolLogViewerProps {
  logs: ToolInvocationLogResponse[];
}

/**
 * Renders the MCP tool-invocation trace — one row per tool call, with its
 * name, arguments, result summary, success indicator and timestamp. Provided
 * as the reference for a live-log view.
 */
const ToolLogViewer = ({ logs }: ToolLogViewerProps) => {
  if (logs.length === 0) {
    return (
      <Typography color='text.secondary'>
        No tool invocations recorded yet.
      </Typography>
    );
  }

  return (
    <List dense>
      {logs.map((log) => (
        <ListItem key={log.id} divider alignItems='flex-start'>
          <Box sx={{ mr: 1, mt: 0.5 }}>
            {log.successful ? <CheckCircleIcon color='success' fontSize='small'/> : <ErrorIcon color='error' fontSize='small'/>}
          </Box>
          <ListItemText
            primary={
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                <Chip label={log.toolName} size='small'/>
                <Typography variant='caption' color='text.secondary'>
                  {new Date(log.occurredAt).toLocaleString()}
                </Typography>
              </Box>
            }
            secondary={
              <>
                {log.arguments && (
                  <Typography variant='body2' component='span' sx={{ display: 'block', fontFamily: 'monospace' }}>
                    args: {log.arguments}
                  </Typography>
                )}
                {log.resultSummary && (
                  <Typography variant='body2' component='span' color='text.secondary'>
                    {log.resultSummary}
                  </Typography>
                )}
              </>
            }
          />
        </ListItem>
      ))}
    </List>
  );
};

export default ToolLogViewer;

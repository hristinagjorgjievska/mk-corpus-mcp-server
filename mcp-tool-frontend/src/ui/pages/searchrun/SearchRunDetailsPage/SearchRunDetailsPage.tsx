import { Box, Chip, CircularProgress, Link, List, ListItem, ListItemText, Paper, Typography } from '@mui/material';
import { useParams } from 'react-router';
import useSearchRunDetails from '../../../../hooks/useSearchRunDetails.ts';
import { Link as RouterLink } from 'react-router';
import { colors } from '../../../../theme/theme.ts';

/**
 * One search run in detail: its status, the resources it produced, and the
 * live MCP tool-invocation trace. Provided as the reference for a detail page.
 */
const SearchRunDetailsPage = () => {
  const { id } = useParams<{ id: string }>();
  const { searchRun, resources, loading } = useSearchRunDetails(id!);

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
        <CircularProgress/>
      </Box>
    );
  }

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3 }}>
      <Box>
        <Typography variant='h5' gutterBottom>
          Ново Пребарување #{id}
        </Typography>
        {searchRun && (
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
            <Typography variant='subtitle1'>“{searchRun.query}”</Typography>
            <Chip label={searchRun.status} size='small'/>
              <Typography variant='body2' color='text.secondary'>
                  {searchRun.resultCount ?? 0} {(searchRun.resultCount ?? 0) === 1 ? 'ресурс' : 'ресурси'}
              </Typography>
          </Box>
        )}
      </Box>

      <Paper variant='outlined' sx={{ p: 2 }}>
        <Typography variant='h6' gutterBottom>Статии</Typography>
        {resources.length === 0 && (
          <Typography color='text.secondary'>Не се пронајдени статии за ова пребарување.</Typography>
        )}
        <List dense>
          {resources.map((resource) => (
            <ListItem key={resource.id} divider>
              <ListItemText
                primary={resource.title ?? `Статија #${resource.id}`}
                secondary={
                  resource.sourceUrl
                    ? <Link href={resource.sourceUrl} target='_blank' rel='noreferrer'>{resource.sourceUrl}</Link>
                    : null
                }
              />
              {resource.macedonianConfidence != null && (
                <Chip
                  label={`MK ${(resource.macedonianConfidence * 100).toFixed(0)}%`}
                  size='small'
                  sx={{ ml: 1 }}
                />
              )}
            </ListItem>
          ))}
        </List>
      </Paper>

        <Box sx={{ backgroundColor: colors.linen, p: 2, mt: 2 }}>
            <Typography sx={{color: colors.muted }}>
                Деталите на сите MCP повици (кој агент што повикал и со кои аргументи) е на страницата{' '}
                <RouterLink to='/playground' style={{ color: colors.red, fontWeight: 800 }}>MCP →</RouterLink>
            </Typography>
        </Box>
    </Box>
  );
};

export default SearchRunDetailsPage;

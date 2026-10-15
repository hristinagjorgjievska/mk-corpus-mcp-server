import { useCallback, useEffect, useRef, useState } from 'react';
import searchRunApi from '../api/searchRunApi.ts';
import toolLogApi from '../api/toolLogApi.ts';
import type { SearchRunResponse } from '../api/types/searchRun.ts';
import type { ResourceResponse } from '../api/types/resource.ts';
import type { ToolInvocationLogResponse } from '../api/types/toolLog.ts';
import useSnackbar from './useSnackbar.ts';

/**
 * Loads one search run together with its resources and its MCP tool-invocation
 * trace, and keeps the trace live by polling every few seconds while the run is
 * still RUNNING. Provided as the reference data hook for a detail page.
 */
const useSearchRunDetails = (id: string) => {
  const { showSnackbar } = useSnackbar();

  const [searchRun, setSearchRun] = useState<SearchRunResponse | null>(null);
  const [resources, setResources] = useState<ResourceResponse[]>([]);
  const [logs, setLogs] = useState<ToolInvocationLogResponse[]>([]);
  const [loading, setLoading] = useState<boolean>(true);

  const loadedOnce = useRef<boolean>(false);

  const fetchLogs = useCallback(async () => {
    try {
      const response = await toolLogApi.findAll(Number(id));
      setLogs(response.data);
    } catch {
      // Ignore polling errors; the initial load already reported failures.
    }
  }, [id]);

  const fetchAll = useCallback(async () => {
    if (!loadedOnce.current) {
      setLoading(true);
    }
    try {
      const [runResponse, resourcesResponse] = await Promise.all([
        searchRunApi.findById(id),
        searchRunApi.findResources(id)
      ]);
      setSearchRun(runResponse.data);
      setResources(resourcesResponse.data);
      await fetchLogs();
    } catch (err) {
      showSnackbar(err instanceof Error ? err.message : 'Failed to load search run.', 'error');
    } finally {
      loadedOnce.current = true;
      setLoading(false);
    }
  }, [id, fetchLogs, showSnackbar]);

  useEffect(() => {
    void fetchAll();
  }, [fetchAll]);

  useEffect(() => {
    if (searchRun?.status !== 'RUNNING') {
      return;
    }
    const interval = setInterval(() => {
      void fetchLogs();
    }, 3000);
    return () => clearInterval(interval);
  }, [searchRun?.status, fetchLogs]);

  return { searchRun, resources, logs, loading, refresh: fetchAll };
};

export default useSearchRunDetails;

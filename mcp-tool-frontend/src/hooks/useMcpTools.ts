import { useCallback, useEffect, useState } from 'react';
import mcpApi from '../api/mcpApi.ts';
import type { InvokeToolResponse, McpToolDescriptor } from '../api/types/mcp.ts';
import useSnackbar from './useSnackbar.ts';

/**
 * Powers the MCP playground: lists the tools the server exposes and invokes
 * them by name. Provided as a fully-working reference — it talks to the same
 * tools an MCP agent (e.g. the Vezilka host) would call.
 */
const useMcpTools = () => {
  const { showSnackbar } = useSnackbar();

  const [tools, setTools] = useState<McpToolDescriptor[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [invoking, setInvoking] = useState<boolean>(false);

  const fetchTools = useCallback(async () => {
    setLoading(true);
    try {
      const response = await mcpApi.listTools();
      setTools(response.data);
    } catch (err) {
      showSnackbar(err instanceof Error ? err.message : 'Failed to load MCP tools.', 'error');
    } finally {
      setLoading(false);
    }
  }, [showSnackbar]);

  const invoke = useCallback(async (name: string, args: Record<string, unknown>): Promise<InvokeToolResponse | null> => {
    setInvoking(true);
    try {
      const response = await mcpApi.invokeTool(name, args);
      return response.data;
    } catch (err) {
      showSnackbar(err instanceof Error ? err.message : 'Failed to invoke tool.', 'error');
      return null;
    } finally {
      setInvoking(false);
    }
  }, [showSnackbar]);

  useEffect(() => {
    void fetchTools();
  }, [fetchTools]);

  return { tools, loading, invoking, invoke };
};

export default useMcpTools;

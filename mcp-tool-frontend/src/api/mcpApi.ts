import axiosInstance from '../axios/axios.ts';
import type { InvokeToolResponse, McpToolDescriptor } from './types/mcp.ts';

const mcpApi = {
  listTools: async () => {
    return await axiosInstance.get<McpToolDescriptor[]>('/mcp/tools');
  },
  invokeTool: async (name: string, args: Record<string, unknown>) => {
    return await axiosInstance.post<InvokeToolResponse>(`/mcp/tools/${name}/invoke`, args);
  }
};

export default mcpApi;

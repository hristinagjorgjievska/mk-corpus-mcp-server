import axiosInstance from '../axios/axios.ts';
import type { ToolInvocationLogResponse } from './types/toolLog.ts';

const toolLogApi = {
  findAll: async (searchRunId?: number) => {
    return await axiosInstance.get<ToolInvocationLogResponse[]>('/tool-logs', {
      params: searchRunId != null ? { searchRunId } : {}
    });
  }
};

export default toolLogApi;

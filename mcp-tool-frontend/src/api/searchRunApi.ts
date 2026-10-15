import axiosInstance from '../axios/axios.ts';
import type { RunSearchRequest, SearchRunResponse } from './types/searchRun.ts';
import type { ResourceResponse } from './types/resource.ts';

const searchRunApi = {
  findAll: async () => {
    return await axiosInstance.get<SearchRunResponse[]>('/search-runs');
  },
  findById: async (id: string) => {
    return await axiosInstance.get<SearchRunResponse>(`/search-runs/${id}`);
  },
  run: async (data: RunSearchRequest) => {
    return await axiosInstance.post<SearchRunResponse>('/search-runs/run', data);
  },
  findResources: async (id: string) => {
    return await axiosInstance.get<ResourceResponse[]>(`/search-runs/${id}/resources`);
  }
};

export default searchRunApi;

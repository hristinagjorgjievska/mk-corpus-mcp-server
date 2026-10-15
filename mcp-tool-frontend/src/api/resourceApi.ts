import axiosInstance from '../axios/axios.ts';
import type { PageResponse, ResourceFilter, ResourceResponse } from './types/resource.ts';

const resourceApi = {
  findAll: async (filter: ResourceFilter, page: number, size: number) => {
    return await axiosInstance.get<PageResponse<ResourceResponse>>('/resources', {
      params: { ...filter, page, size }
    });
  },
  findById: async (id: string) => {
    return await axiosInstance.get<ResourceResponse>(`/resources/${id}`);
  },
  analyze: async (id: string) => {
    return await axiosInstance.post<ResourceResponse>(`/resources/${id}/analyze`);
  },
  delete: async (id: string) => {
    return await axiosInstance.delete<ResourceResponse>(`/resources/${id}/delete`);
  }
};

export default resourceApi;

import axiosInstance from '../axios/axios.ts';
import type { CorpusStats } from './types/stats.ts';

const statsApi = {
  get: async () => {
    return await axiosInstance.get<CorpusStats>('/stats');
  }
};

export default statsApi;

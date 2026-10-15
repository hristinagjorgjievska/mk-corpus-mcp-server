import axiosInstance from '../axios/axios.ts';
import type { NegotiationMessage } from './types/negotiation.ts';

const negotiationApi = {
    run: async () => {
        return await axiosInstance.post<NegotiationMessage[]>('/negotiation');
    }
};

export default negotiationApi;
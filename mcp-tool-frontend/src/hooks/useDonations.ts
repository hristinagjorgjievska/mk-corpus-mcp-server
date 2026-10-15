import { useCallback, useEffect, useState } from 'react';
import donationApi from '../api/donationApi.ts';
import type { CreateDonationBatchRequest, DonationBatchResponse } from '../api/types/donation.ts';
import useSnackbar from './useSnackbar.ts';

const useDonations = () => {

    const { showSnackbar } = useSnackbar();
    const [donations, setDonations] = useState<DonationBatchResponse[]>([]);
    const [loading, setLoading] = useState<boolean>(false);

    const refresh = useCallback(async () => {
        setLoading(true);
        try {
            const response = await donationApi.findAll();
            setDonations(response.data);
        } catch (err) {
            showSnackbar(err instanceof Error ? err.message : 'Пакетите не може да се вчитаат.', 'error');
        } finally {
            setLoading(false);
        }
    }, [showSnackbar]);

    const onCreate = useCallback(async (data: CreateDonationBatchRequest) => {
        try {
            await donationApi.add(data);
            showSnackbar('Пакетот е креиран.', 'success');
            await refresh();
        } catch (err) {
            showSnackbar(err instanceof Error ? err.message : 'Пакетот не може да се креира.', 'error');
        }
    }, [refresh, showSnackbar]);

    const onApprove = useCallback(async (id: number) => {
        try {
            await donationApi.approve(String(id));
            showSnackbar('Пакетот е одобрен.', 'success');
            await refresh();
        } catch (err) {
            showSnackbar(err instanceof Error ? err.message : 'Одобрувањето не успеа.', 'error');
        }
    }, [refresh, showSnackbar]);

    const onSubmit = useCallback(async (id: number) => {
        try {
            await donationApi.submit(String(id));
            showSnackbar('Пакетот е испратен до Везилка.', 'success');
            await refresh();
        } catch (err) {
            showSnackbar(err instanceof Error ? err.message : 'Испраќањето не успеа.', 'error');
        }
    }, [refresh, showSnackbar]);

    useEffect(() => {
        void refresh();
    }, [refresh]);

    return { donations, loading, onCreate, onApprove, onSubmit };
};

export default useDonations;


import type { PageResponse, ResourceFilter, ResourceResponse } from '../api/types/resource.ts';
import { useCallback, useEffect, useState } from 'react';
import resourceApi from '../api/resourceApi.ts';
import useSnackbar from './useSnackbar.ts';

const useResources = (filter: ResourceFilter, page: number, size: number) => {
    const { showSnackbar } = useSnackbar();

    const [resources, setResources] = useState<PageResponse<ResourceResponse> | null>(null);
    const [loading, setLoading] = useState<boolean>(true);

    const refresh = useCallback(async () => {
        setLoading(true);
        try {
            const response = await resourceApi.findAll(filter, page, size);
            setResources(response.data);
        } catch (err) {
            showSnackbar(err instanceof Error ? err.message : 'Ресурсите не може да се вчитаат.', 'error');
        } finally {
            setLoading(false);
        }
    }, [filter, page, size, showSnackbar]);

    const onDelete = useCallback(async (id: number) => {
        try {
            await resourceApi.delete(String(id));
            showSnackbar('Статијата е избришана.', 'success');
            await refresh();
        } catch (err) {
            showSnackbar(err instanceof Error ? err.message : 'Бришењето не успеа.', 'error');
        }
    }, [refresh, showSnackbar]);

    const onAnalyze = useCallback(async (id: number) => {
        try {
            await resourceApi.analyze(String(id));
            showSnackbar('Статијата е прочитана.', 'success');
            await refresh();
        } catch (err) {
            showSnackbar(err instanceof Error ? err.message : 'Анализата не успеа.', 'error');
        }
    }, [refresh, showSnackbar]);

    useEffect(() => {
        void refresh();
    }, [refresh]);

    return { resources, loading, onDelete, onAnalyze };
};

export default useResources;

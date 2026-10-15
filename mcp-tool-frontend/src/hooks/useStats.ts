import { useState } from 'react';
import type { CorpusStats } from '../api/types/stats.ts';
import { useEffect} from "react";
import statsApi from '../api/statsApi.ts';
import useAuth from './useAuth.ts';
import useSnackbar from './useSnackbar.ts';

const useStats = () => {
    const { isLoggedIn } = useAuth();
    const { showSnackbar } = useSnackbar();

    const [stats, setStats] = useState<CorpusStats | null>(null);
    const [loading, setLoading] = useState<boolean>(false);

    useEffect(() => {
        if (!isLoggedIn) {
            setStats(null);
            return;
        }

        setLoading(true);
        statsApi.get()
            .then((response) => setStats(response.data))
            .catch((err) => showSnackbar(err instanceof Error ? err.message : 'Статистиката не може да се вчита.', 'error'))
            .finally(() => setLoading(false));
    }, [isLoggedIn, showSnackbar]);

  return { stats, loading };
};

export default useStats;

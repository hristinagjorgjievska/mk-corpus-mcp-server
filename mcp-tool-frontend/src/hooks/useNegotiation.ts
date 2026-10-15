import { useEffect, useState } from 'react';
import negotiationApi from '../api/negotiationApi.ts';
import type { NegotiationMessage } from '../api/types/negotiation.ts';
import useSnackbar from './useSnackbar.ts';

const useNegotiation = () => {
    const { showSnackbar } = useSnackbar();

    const [messages, setMessages] = useState<NegotiationMessage[]>([]);
    const [visible, setVisible] = useState<number>(0);
    const [loading, setLoading] = useState<boolean>(false);

    useEffect(() => {
        if (visible >= messages.length) return;
        const timer = setTimeout(() => setVisible((v) => v + 1), 700);
        return () => clearTimeout(timer);
    }, [visible, messages]);

    const onRun = () => {
        setLoading(true);
        setMessages([]);
        setVisible(0);
        negotiationApi.run()
            .then((response) => setMessages(response.data))
            .catch(() => showSnackbar('Агентот не успеа да ги анализира дупките.', 'error'))
            .finally(() => setLoading(false));
    };

    return { messages: messages.slice(0, visible), loading, onRun };
};

export default useNegotiation;
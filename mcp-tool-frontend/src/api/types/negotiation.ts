export interface CategoryNeed {
    category: string;
    target: number;
    received: number;
}

export interface NegotiationMessage {
    from: 'corpus' | 'agent';
    text: string;
    needs: CategoryNeed[] | null;
}
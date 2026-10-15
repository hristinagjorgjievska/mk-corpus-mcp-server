export type SearchStatus = 'PENDING' | 'RUNNING' | 'COMPLETED' | 'FAILED';

export interface RunSearchRequest {
  query: string;
  limit?: number;
}

export interface SearchRunResponse {
  id: number;
  query: string;
  status: SearchStatus;
  startedAt: string | null;
  finishedAt: string | null;
  resultCount: number | null;
  createdAt: string;
}

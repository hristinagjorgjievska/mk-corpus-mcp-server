import { createContext } from 'react';
import type { RunSearchRequest, SearchRunResponse } from '../api/types/searchRun.ts';

export interface SearchRunsContextType {
  searchRuns: SearchRunResponse[];
  loading: boolean;
  onRun: (data: RunSearchRequest) => Promise<void>;
  refresh: () => Promise<void>;
}

const SearchRunsContext = createContext<SearchRunsContextType>({} as SearchRunsContextType);

export default SearchRunsContext;

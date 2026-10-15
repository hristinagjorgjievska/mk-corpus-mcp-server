import { useContext } from 'react';
import SearchRunsContext, { type SearchRunsContextType } from '../contexts/searchRunsContext.ts';

const useSearchRuns = () => useContext<SearchRunsContextType>(SearchRunsContext);

export default useSearchRuns;

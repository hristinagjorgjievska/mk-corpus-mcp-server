export interface ResourceAnalysisResponse {
  id: number;
  resourceId: number;
  summary: string | null;
  keywords: string | null;
  sentenceCount: number | null;
  macedonianConfidence: number | null;
  analyzedAt: string | null;
}

export interface ResourceResponse {
  id: number;
  searchRunId: number;
  externalId: string | null;
  title: string | null;
  content: string | null;
  sourceUrl: string | null;
  language: string | null;
  macedonianConfidence: number | null;
  wordCount: number | null;
  fetchedAt: string | null;
  analysis: ResourceAnalysisResponse | null;
  donationBatchId: number | null;
}

/**
 * Optional filters for browsing resources — mirrors the request params of
 * GET /api/resources. Omitted fields are not sent.
 */
export interface ResourceFilter {
  searchRunId?: number;
  minMacedonianConfidence?: number;
  analyzed?: boolean;
  donated?: boolean;
  search?: string;
}

/**
 * Spring Data Page<T> as serialised by the backend.
 */
export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

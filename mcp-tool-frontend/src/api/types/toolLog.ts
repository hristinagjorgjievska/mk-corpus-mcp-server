export interface ToolInvocationLogResponse {
  id: number;
  toolName: string;
  arguments: string | null;
  resultSummary: string | null;
  successful: boolean;
  occurredAt: string;
  searchRunId: number | null;
}

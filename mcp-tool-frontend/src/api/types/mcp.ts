export interface McpToolDescriptor {
  name: string;
  description: string;
  inputSchema: string;
}

export interface InvokeToolResponse {
  tool: string;
  arguments: string;
  successful: boolean;
  result: string | null;
  error: string | null;
}

export type DonationStatus =
  | 'DRAFT'
  | 'APPROVED'
  | 'SUBMITTED'
  | 'ACCEPTED'
  | 'REJECTED'
  | 'FAILED';

export interface CreateDonationBatchRequest {
  resourceIds: number[];
}

export interface DonationBatchResponse {
  id: number;
  status: DonationStatus;
  vezilkaReference: string | null;
  submittedAt: string | null;
  resourceCount: number;
  resourceIds: number[];
  createdAt: string;
}

import { MedicalRecordResponse } from './MedicalRecordResponse';

export interface TransactionResponse
{
   transactionId: string;
   record: MedicalRecordResponse;
   senderPubKey: string;
   senderAddress: string;
   recipientAddress: string;
   signature: string;
}

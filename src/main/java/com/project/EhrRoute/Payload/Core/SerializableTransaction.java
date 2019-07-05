package com.project.EhrRoute.Payload.Core;
import com.project.EhrRoute.Entities.EHR.MedicalRecord;

public class SerializableTransaction
{
    private String transactionId;
    private MedicalRecord record;
    private String senderAddress;
    private String recipientAddress;
    private String signature;

    public SerializableTransaction() { }
    public SerializableTransaction(String transactionId, MedicalRecord record, String senderAddress, String recipientAddress, String signature) {
        this.transactionId = transactionId;
        this.record = record;
        this.senderAddress = senderAddress;
        this.recipientAddress = recipientAddress;
        this.signature = signature;
    }

    public String getSignature() {
        return signature;
    }
    public MedicalRecord getRecord() {
        return record;
    }
    public String getSenderAddress() {
        return senderAddress;
    }
    public String getTransactionId() {
        return transactionId;
    }
    public String getRecipientAddress() {
        return recipientAddress;
    }
    public void setRecord(MedicalRecord record) {
        this.record = record;
    }
    public void setSignature(String signature) {
        this.signature = signature;
    }
    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    @Override
    public String toString() {
        return "SerializableTransaction{" +
                "transactionId='" + transactionId + '\'' +
                ", record=" + record +
                ", senderAddress='" + senderAddress + '\'' +
                ", recipientAddress='" + recipientAddress + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }
}

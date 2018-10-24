package com.project.EMRChain.Core;
import com.project.EMRChain.Core.Utilities.StringUtil;
import com.project.EMRChain.EHR.MedicalRecord;
import com.project.EMRChain.Utilities.JsonUtil;

import java.security.PublicKey;

public class Transaction
{
    private String transactionId; // Hash of transaction
    private MedicalRecord record;
    private PublicKey sender; // Sender's address/public key.
    private PublicKey recipient; // Recipient's address/public key.
    private byte[] signature;


    public Transaction() { }
    public Transaction(MedicalRecord record, PublicKey sender, PublicKey recipient)
    {
        this.record = record;
        this.sender = sender;
        this.recipient = recipient;
    }

    public String getTransactionData()
    {
        String data =
        JsonUtil.createJson(record) +
        StringUtil.getStringFromKey(sender) +
        StringUtil.getStringFromKey(recipient);
        return data;
    }

    public byte[] getSignature() {
        return signature;
    }
    public void setSignature(byte[] signature) {
        this.signature = signature;
    }
    public PublicKey getSender() {
        return sender;
    }
    public PublicKey getRecipient() {
        return recipient;
    }
    public MedicalRecord getRecord() {
        return record;
    }
    public void setRecord(MedicalRecord record) {
        this.record = record;
    }
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    public void setSender(PublicKey sender) {
        this.sender = sender;
    }
    public void setRecipient(PublicKey recipient) {
        this.recipient = recipient;
    }
}
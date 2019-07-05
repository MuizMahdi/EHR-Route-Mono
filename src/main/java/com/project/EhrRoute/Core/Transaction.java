package com.project.EhrRoute.Core;
import com.project.EhrRoute.Core.Utilities.KeyUtil;
import com.project.EhrRoute.Entities.EHR.MedicalRecord;
import com.project.EhrRoute.Utilities.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.security.GeneralSecurityException;
import java.security.PublicKey;


@Component
public class Transaction
{
    private JsonUtil jsonUtil;
    private KeyUtil keyUtil;

    private byte[] transactionId; // Hash of transaction
    private MedicalRecord record;
    private Address senderAddress;
    private Address recipientAddress;
    private byte[] signature;


    @Autowired
    public Transaction(JsonUtil jsonUtil, KeyUtil keyUtil) {
        this.jsonUtil = jsonUtil;
        this.keyUtil = keyUtil;
    }

    public Transaction() { }
    public Transaction(MedicalRecord record, Address senderAddress, Address recipientAddress) throws GeneralSecurityException {
        this.record = record;
        this.recipientAddress = recipientAddress;
        this.senderAddress = senderAddress;
    }

    public String getTransactionData()
    {
        String data =
        jsonUtil.createJson(record) +
        senderAddress.getAddress() +
        recipientAddress.getAddress();

        return data;
    }

    public byte[] getSignature() {
        return signature;
    }
    public void setSignature(byte[] signature) {
        this.signature = signature;
    }
    public MedicalRecord getRecord() {
        return record;
    }
    public void setRecord(MedicalRecord record) {
        this.record = record;
    }
    public void setTransactionId(byte[] transactionId) {
        this.transactionId = transactionId;
    }
    public byte[] getTransactionId() {
        return transactionId;
    }
    public Address getSenderAddress() {
        return senderAddress;
    }
    public void setSenderAddress(Address senderAddress) {
        this.senderAddress = senderAddress;
    }
    public Address getRecipientAddress() {
        return recipientAddress;
    }
    public void setRecipientAddress(Address recipientAddress) {
        this.recipientAddress = recipientAddress;
    }
}

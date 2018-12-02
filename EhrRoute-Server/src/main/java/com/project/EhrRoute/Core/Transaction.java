package com.project.EMRChain.Core;
import com.project.EMRChain.Core.Utilities.StringUtil;
import com.project.EMRChain.Entities.EHR.MedicalRecord;
import com.project.EMRChain.Utilities.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.security.PublicKey;

@Component
public class Transaction
{
    private StringUtil stringUtil;
    private JsonUtil jsonUtil;

    private String transactionId; // Hash of transaction
    private MedicalRecord record;
    private PublicKey senderPubKey; // Sender's public key.
    private Address senderAddress;
    private Address recipientAddress;
    private byte[] signature;


    @Autowired
    public Transaction(StringUtil stringUtil, JsonUtil jsonUtil)
    {
        this.stringUtil = stringUtil;
        this.jsonUtil = jsonUtil;
    }

    public Transaction() { }

    public Transaction(MedicalRecord record, PublicKey senderPubKey, Address recipientAddress) {
        this.record = record;
        this.senderPubKey = senderPubKey;
        this.recipientAddress = recipientAddress;
        this.senderAddress = new Address();
        this.senderAddress.generateAddress(senderPubKey);
    }

    public String getTransactionData()
    {
        JsonUtil jsonUtil1 = new JsonUtil();
        StringUtil stringUtil1 = new StringUtil();

        String data =
        jsonUtil1.createJson(record) +
        stringUtil1.getStringFromKey(senderPubKey) +
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
    public PublicKey getSenderPubKey() {
        return senderPubKey;
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
    public void setSenderPubKey(PublicKey senderPubKey) {
        this.senderPubKey = senderPubKey;
    }
    public String getTransactionId() {
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

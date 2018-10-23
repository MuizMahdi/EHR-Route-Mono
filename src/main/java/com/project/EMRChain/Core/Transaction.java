package com.project.EMRChain.Core;
import com.project.EMRChain.Core.Utilities.EcdsaUtil;
import com.project.EMRChain.Core.Utilities.HashUtil;
import com.project.EMRChain.Core.Utilities.StringUtil;
import com.project.EMRChain.EHR.MedicalRecord;
import com.project.EMRChain.EHR.PatientInfo;
import com.project.EMRChain.Utilities.JsonUtil;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;

public class Transaction
{
    private String transactionId; // Hash of transaction
    private MedicalRecord transactionData;

    private PublicKey sender; // Sender's address/public key.
    private PublicKey recipient; // Recipient's address/public key.
    public byte[] signature;
    private int sequence = 0; // A count of the number of transactions

    //private ArrayList<TransactionInput> inputs = new ArrayList<>();
    //private ArrayList<TransactionOutput> outputs = new ArrayList<>();

    public Transaction() { }
    public Transaction(MedicalRecord transactionData, PublicKey sender, PublicKey recipient) {
        this.transactionData = transactionData;
        this.sender = sender;
        this.recipient = recipient;
        //this.inputs = inputs;
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
    public MedicalRecord getTransactionData() {
        return transactionData;
    }
    public int getSequence() {
        return sequence;
    }
    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
    public void setTransactionData(MedicalRecord transactionData) {
        this.transactionData = transactionData;
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
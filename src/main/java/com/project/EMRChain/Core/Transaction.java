package com.project.EMRChain.Core;
import com.project.EMRChain.Core.Utilities.HashUtil;
import com.project.EMRChain.EHR.MedicalRecord;
import com.project.EMRChain.EHR.PatientInfo;
import com.project.EMRChain.Utilities.JsonUtil;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;

public class Transaction
{
    private String transactionId; // Hash of transaction
    private MedicalRecord data;

    private PublicKey sender; // Sender's address/public key.
    private PublicKey recipient; // Recipient's address/public key.
    public byte[] signature;
    private static int sequence = 0; // A count of the number of transactions

    private ArrayList<TransactionInput> inputs = new ArrayList<>();
    private ArrayList<TransactionOutput> outputs = new ArrayList<>();

    public Transaction() { }
    public Transaction(MedicalRecord data, PublicKey sender, PublicKey recipient, ArrayList<TransactionInput> inputs) {
        this.data = data;
        this.sender = sender;
        this.recipient = recipient;
        this.inputs = inputs;
    }

    private byte[] getHash() {
        sequence++; // Increase sequence to avoid 2 transactions having same hash

        if (data == null) {
           data = new MedicalRecord();
        }

        String data = JsonUtil.createJson(data);
        String keys = ""; // Todo: convert keys into strings (stringUtil) and add them.

        byte[] hashedData = (data + keys + sequence).getBytes();

        return HashUtil.SHA256(hashedData);
    }
}
package com.project.EMRChain.Core;
import com.project.EMRChain.EHR.MedicalRecord;
import java.util.ArrayList;

public class Transaction
{
    private ArrayList<TransactionInput> inputs = new ArrayList<>();
    private ArrayList<TransactionOutput> outputs = new ArrayList<>();

    private String transactionId; // Hash of transaction
    private MedicalRecord data;
}

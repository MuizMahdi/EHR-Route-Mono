package com.project.EMRChain.Core.Utilities;
import com.project.EMRChain.Core.Transaction;
import com.project.EMRChain.EHR.MedicalRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class HashUtil
{
    private Logger logger = LoggerFactory.getLogger(HashUtil.class);

    public byte[] SHA256(byte[] input)
    {
        try
        {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(input);
        }
        catch (NoSuchAlgorithmException exception)
        {
            logger.error(exception.getMessage());
            return null;
        }
    }

    public byte[] SHA256Twice(byte[] input, int offset, int length)
    {
        try
        {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(input, offset, length);
            return digest.digest(digest.digest());
        }
        catch (NoSuchAlgorithmException exception)
        {
            logger.error(exception.getMessage());
            return null;
        }
    }

    public byte[] hashTransactionData(Transaction transaction)
    {
        if (transaction.getTransactionData() == null) {
            transaction.setRecord(new MedicalRecord());
        }

        byte[] dataBytes = transaction.getTransactionData().getBytes();

        return SHA256(dataBytes);
    }

    public String toString(byte[] hash)
    {
        StringBuffer hexString = new StringBuffer();

        for (byte b: hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }
}
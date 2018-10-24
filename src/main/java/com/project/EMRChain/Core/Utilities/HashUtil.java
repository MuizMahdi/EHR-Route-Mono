package com.project.EMRChain.Core.Utilities;
import com.project.EMRChain.Core.Transaction;
import com.project.EMRChain.EHR.MedicalRecord;
import com.project.EMRChain.Utilities.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil
{
    private static Logger logger = LoggerFactory.getLogger(HashUtil.class);

    public static byte[] SHA256(byte[] input)
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

    public static String toString(byte[] hash)
    {
        StringBuffer hexString = new StringBuffer();

        for (byte b: hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }

    public static byte[] hashTransactionData(Transaction transaction)
    {
        if (transaction.getTransactionData() == null) {
            transaction.setRecord(new MedicalRecord());
        }

        byte[] dataBytes = transaction.getTransactionData().getBytes();

        return HashUtil.SHA256(dataBytes);
    }
}
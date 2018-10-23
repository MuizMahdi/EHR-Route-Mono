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

    public static byte[] hashTransaction(Transaction transaction)
    {
        // int transactionSequence = transaction.getSequence();  Todo: This did not work.
        // transaction.setSequence(transactionSequence++);       Todo: Figure out why.

        // Increase sequence to avoid 2 transactions having same hash
        transaction.setSequence(transaction.getSequence()+1); // This works though.

        if (transaction.getTransactionData() == null) {
            transaction.setTransactionData(new MedicalRecord());
        }

        String stringData =
        JsonUtil.createJson(transaction.getTransactionData()) +
        StringUtil.getStringFromKey(transaction.getSender()) +
        StringUtil.getStringFromKey(transaction.getRecipient()) +
        transaction.getSequence();

        byte[] dataBytes = stringData.getBytes();

        return HashUtil.SHA256(dataBytes);
    }
}
package com.project.EhrRoute.Core.Utilities;
import com.project.EhrRoute.Core.Transaction;
import com.project.EhrRoute.Entities.EHR.MedicalRecord;
import com.project.EhrRoute.Exceptions.ResourceEmptyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
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

    public byte[] hashTransactionData(Transaction transaction) throws ResourceEmptyException
    {
        if (transaction.getTransactionData() == null)
        {
            transaction.setRecord(new MedicalRecord());

            if (transaction.getSenderAddress() == null || transaction.getRecipientAddress() == null) {
                throw new ResourceEmptyException("Transaction data hashing failed, transaction Sender or Recipient not found");
            }
        }

        byte[] dataBytes = transaction.getTransactionData().getBytes(StandardCharsets.UTF_8);

        return SHA256(dataBytes);
    }
}

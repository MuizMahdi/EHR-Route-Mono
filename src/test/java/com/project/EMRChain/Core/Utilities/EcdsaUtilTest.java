package com.project.EMRChain.Core.Utilities;
import com.project.EMRChain.Core.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EcdsaUtilTest
{
    private final EcdsaUtil ecdsaUtil = new EcdsaUtil();

    @Test
    public void ecSign() throws Exception
    {
        KeyPair keyPair = ecdsaUtil.ecGenerateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();

        Transaction transaction = mock(Transaction.class);
        when(transaction.getTransactionData()).thenReturn("TransactionData");

        assertNotNull(ecdsaUtil.ecSign(privateKey,transaction));
        assertTrue(ecdsaUtil.ecSign(privateKey,transaction) instanceof byte[]);
    }

    @Test
    public void testEcVerifyTransactionWithFalseSignature() throws Exception
    {
        KeyPair keyPair = ecdsaUtil.ecGenerateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        Transaction transaction = mock(Transaction.class);

        when(transaction.getTransactionData()).thenReturn("TransactionData");

        byte[] signature = ecdsaUtil.ecSign(privateKey, transaction);

        when(transaction.getSignature()).thenReturn(signature);

        assertFalse(ecdsaUtil.ecVerifyTransactionSignature(publicKey, transaction));
    }

    @Test(expected = Exception.class)
    public void testEcVerifyTransactionSignatureThrowsException() throws Exception
    {
        KeyPair keyPair = ecdsaUtil.ecGenerateKeyPair();
        PublicKey publicKey = keyPair.getPublic();

        Transaction transaction = mock(Transaction.class);

        when(transaction.getTransactionData()).thenReturn("TransactionData");

        assertFalse(ecdsaUtil.ecVerifyTransactionSignature(publicKey, transaction));

        when(transaction.getSignature()).thenReturn("Signature".getBytes());

        ecdsaUtil.ecVerifyTransactionSignature(publicKey, transaction);
    }

    @Test
    public void ecGenerateKeyPair() throws Exception
    {
        assertNotNull(ecdsaUtil.ecGenerateKeyPair());
        assertTrue(ecdsaUtil.ecGenerateKeyPair() instanceof KeyPair);
        assertTrue(ecdsaUtil.ecGenerateKeyPair().getPublic() instanceof PublicKey);
        assertTrue(ecdsaUtil.ecGenerateKeyPair().getPrivate() instanceof PrivateKey);
    }

}
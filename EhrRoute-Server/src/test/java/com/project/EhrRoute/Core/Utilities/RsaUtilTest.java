package com.project.EhrRoute.Core.Utilities;
import com.project.EhrRoute.Core.Transaction;
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
public class RsaUtilTest
{
    private final RsaUtil rsaUtil = new RsaUtil();

    @Test
    public void rsaSign() throws Exception
    {
        KeyPair keyPair = rsaUtil.rsaGenerateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();

        Transaction transaction = mock(Transaction.class);
        when(transaction.getTransactionData()).thenReturn("TransactionData");

        assertNotNull(rsaUtil.rsaSign(privateKey,transaction));
        assertTrue(rsaUtil.rsaSign(privateKey, transaction) instanceof byte[]);
    }

    @Test
    public void testRsaVerifyTransactionWithFalseSignature() throws Exception
    {
        KeyPair keyPair = rsaUtil.rsaGenerateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        Transaction transaction = mock(Transaction.class);

        when(transaction.getTransactionData()).thenReturn("TransactionData");

        byte[] signature = rsaUtil.rsaSign(privateKey, transaction);

        when(transaction.getSignature()).thenReturn(signature);

        assertFalse(rsaUtil.rsaVerifyTransactionSignature(publicKey, transaction));
    }

    @Test(expected = Exception.class)
    public void testRsaVerifyTransactionSignatureThrowsException() throws Exception
    {
        KeyPair keyPair = rsaUtil.rsaGenerateKeyPair();
        PublicKey publicKey = keyPair.getPublic();

        Transaction transaction = mock(Transaction.class);

        when(transaction.getTransactionData()).thenReturn("TransactionData");

        assertFalse(rsaUtil.rsaVerifyTransactionSignature(publicKey, transaction));

        when(transaction.getSignature()).thenReturn("Signature".getBytes());

        rsaUtil.rsaVerifyTransactionSignature(publicKey, transaction);
    }

    @Test
    public void rsaGenerateKeyPair() throws Exception
    {
        assertNotNull(rsaUtil.rsaGenerateKeyPair());
        assertTrue(rsaUtil.rsaGenerateKeyPair() instanceof KeyPair);
        assertTrue(rsaUtil.rsaGenerateKeyPair().getPublic() instanceof PublicKey);
        assertTrue(rsaUtil.rsaGenerateKeyPair().getPrivate() instanceof PrivateKey);
    }

}
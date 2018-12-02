package com.project.EMRChain.Core.Utilities;
import com.project.EhrRoute.Core.Transaction;
import com.project.EhrRoute.Core.Utilities.HashUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HashUtilTest
{
    private final HashUtil hashUtil = new HashUtil();
    private final byte[] bytes = "any".getBytes();

    @Test
    public void SHA256() throws Exception
    {

        try {
            hashUtil.SHA256(bytes);
        }
        catch (Exception e)
        {
            fail("Exception" + e);
        }

        assertNotNull(hashUtil.SHA256(bytes));
        assertTrue(hashUtil.SHA256(bytes) instanceof byte[]);
        assertNotEquals(hashUtil.SHA256(bytes), hashUtil.SHA256(bytes));
    }

    @Test
    public void SHA256Twice() throws Exception
    {
        try {
            hashUtil.SHA256Twice(bytes, 0, 1);
        }
        catch (Exception e)
        {
            fail("Exception" + e);
        }

        assertNotNull(hashUtil.SHA256Twice(bytes, 0, 1));
        assertTrue(hashUtil.SHA256Twice(bytes, 0, 1) instanceof byte[]);
        assertNotEquals(hashUtil.SHA256Twice(bytes, 0, 1) , hashUtil.SHA256Twice(bytes, 0, 1));

    }

    @Test
    public void hashTransactionData() throws Exception
    {
        Transaction transaction = mock(Transaction.class);
        when(transaction.getTransactionData()).thenReturn(null);
        when(transaction.getTransactionData()).thenReturn("Data");

        hashUtil.hashTransactionData(transaction);

        assertNotNull(hashUtil.hashTransactionData(transaction));
        assertTrue(hashUtil.hashTransactionData(transaction) instanceof byte[]);
        assertNotEquals(hashUtil.hashTransactionData(transaction), hashUtil.hashTransactionData(transaction));
    }

    @Test(expected = Exception.class)
    public void testHashTransactionThrowsException() throws Exception
    {
        Transaction transaction = mock(Transaction.class);
        when(transaction.getTransactionData()).thenReturn(null);
        when(transaction.getSenderAddress()).thenReturn(null);

        hashUtil.hashTransactionData(transaction);
    }
}
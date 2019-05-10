package com.project.EhrRoute.Core.Utilities;
import com.project.EhrRoute.Core.Address;
import com.project.EhrRoute.Core.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.security.GeneralSecurityException;
import java.security.PublicKey;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddressUtilTest
{
    @Mock
    private RsaUtil rsaUtil;

    @Mock
    private StringUtil stringUtil;

    @Mock
    private HashUtil hashUtil;

    @Mock
    private KeyUtil keyUtil;

    @Mock
    private Base58 base58;

    @InjectMocks
    private AddressUtil addressUtil;


    @Test
    public void testGenerateAddress()
    {
        PublicKey publicKey = mock(PublicKey.class);

        when(keyUtil.getStringFromPublicKey(publicKey)).thenReturn("StringPublicKey");

        when(hashUtil.SHA256((byte[])notNull())).thenReturn("StringPublicKeyHash".getBytes());

        String address = "Address";

        try {
            when(addressUtil.generateAddress(publicKey)).thenReturn(address);

            addressUtil.generateAddress(publicKey);

            assertNotNull(addressUtil.generateAddress(publicKey));
            assertTrue(addressUtil.generateAddress(publicKey) instanceof String);
            assertEquals(addressUtil.generateAddress(publicKey).length(), address.length());
        }
        catch (GeneralSecurityException Ex) {}

    }


    @Test
    public void testConfirmTransactionSenderAddress()
    {

        try {
            PublicKey publicKey = mock(PublicKey.class);
            when(keyUtil.getStringFromPublicKey(publicKey)).thenReturn("StringPublicKey");
            when(hashUtil.SHA256((byte[])notNull())).thenReturn("StringPublicKeyHash".getBytes());

            when(addressUtil.generateAddress(publicKey)).thenReturn("Address");

            Address address = mock(Address.class);
            when(address.getAddress()).thenReturn("Address");

            Transaction transaction = mock(Transaction.class);
            when(transaction.getSenderPubKey()).thenReturn(publicKey);
            when(transaction.getSenderAddress()).thenReturn(address);

            addressUtil.confirmTransactionSenderAddress(transaction);

            assertTrue(addressUtil.confirmTransactionSenderAddress(transaction));
        }
        catch (GeneralSecurityException Ex) {}

    }

}

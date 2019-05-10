package com.project.EhrRoute.Core;
import com.project.EhrRoute.Core.Utilities.AddressUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.security.GeneralSecurityException;
import java.security.PublicKey;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddressTest
{
    @Mock
    private AddressUtil addressUtil;

    @InjectMocks
    private Address address;

    @Test
    public void testAddressGenerated()
    {
        PublicKey publicKey = mock(PublicKey.class);

        try {
            when(addressUtil.generateAddress(publicKey)).thenReturn("Address");

            address.generateAddress(publicKey);

            assertEquals(address.getAddress(), "Address");
        }
        catch (GeneralSecurityException Ex) {}
    }
}

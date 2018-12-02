package com.project.EMRChain.Core;
import com.project.EMRChain.Core.Utilities.AddressUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
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

        when(addressUtil.generateAddress(publicKey)).thenReturn("Address");

        address.generateAddress(publicKey);

        assertEquals(address.getAddress(), "Address");
    }
}

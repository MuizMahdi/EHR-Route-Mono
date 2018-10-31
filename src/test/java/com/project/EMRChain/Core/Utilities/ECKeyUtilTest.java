package com.project.EMRChain.Core.Utilities;
import org.junit.Test;
import static org.junit.Assert.*;
import com.project.EMRChain.Core.Utilities.ECKeyUtil;
import com.project.EMRChain.Core.Utilities.EcdsaUtil;
import com.project.EMRChain.Core.Utilities.StringUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PublicKey;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ECKeyUtilTest
{
    private final ECKeyUtil ecKeyUtil = new ECKeyUtil();

    @Test
    public void getPublicKeyFromString() throws Exception
    {
        String keyString = "MFYwEAYHKoZIzj0CAQYFK4EEAAoDQg" +
        "AEj6KhGX6L9HOt2ClTMAfbYKSNPcEnX7GbmXfc1eRmLG5" +
        "9z24P24BNbHQaKnROshGeDpHLuXl9Xd8ZSuHTYWQeQw==";

        ecKeyUtil.getPublicKeyFromString(keyString);

        assertNotNull(ecKeyUtil.getPublicKeyFromString(keyString));
        assertTrue(ecKeyUtil.getPublicKeyFromString(keyString) instanceof PublicKey);

        StringUtil stringUtil = new StringUtil();
        assertEquals(keyString, stringUtil.getStringFromKey(ecKeyUtil.getPublicKeyFromString(keyString)));
    }

    @Test
    public void getPublicKeyFromBytes() throws Exception {
    }



}
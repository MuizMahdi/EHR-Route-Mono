package com.project.EhrRoute.Core.Utilities;
import org.junit.Test;
import static org.junit.Assert.*;

import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.security.PublicKey;

@RunWith(MockitoJUnitRunner.class)
public class KeyUtilTest
{
    private final KeyUtil keyUtil = new KeyUtil();

    @Test
    public void getPublicKeyFromString() throws Exception
    {
        String keyString = "MFYwEAYHKoZIzj0CAQYFK4EEAAoDQg" +
        "AEj6KhGX6L9HOt2ClTMAfbYKSNPcEnX7GbmXfc1eRmLG5" +
        "9z24P24BNbHQaKnROshGeDpHLuXl9Xd8ZSuHTYWQeQw==";

        keyUtil.getPublicKeyFromString(keyString);

        assertNotNull(keyUtil.getPublicKeyFromString(keyString));
        assertTrue(keyUtil.getPublicKeyFromString(keyString) instanceof PublicKey);

        StringUtil stringUtil = new StringUtil();
        assertEquals(keyString, stringUtil.getStringFromKey(keyUtil.getPublicKeyFromString(keyString)));
    }

    @Test
    public void getPublicKeyFromBytes() throws Exception {
    }



}
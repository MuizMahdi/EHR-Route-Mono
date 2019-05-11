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
        String keyString = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCg" +
        "KCAQEAkOssFhIud24kdlMbjpYh3MJFGyFIQo2D5UisvLm/wJ5" +
        "GJmGiI2R0IelKd11RxqpY+3E/fD6qWGamFdikwM9kms8hCr7A" +
        "DXcl2W8c+qD95/u9OYlLlk+4vAmUUgABBQxp/nCRf8QIIYIZe" +
        "Qn5j3lcn7MmPaa2NMJrhSSC2YnxKfnF5chO4DUhSnsXC+gMMR" +
        "0hUfnBR++3Wq4ziqxALX8u2r7WRI/Hyr2Vm/kQtpgfbMCFNyg" +
        "Iij0yjaz73A8qgO2R9fvf++Z8DULYbEJWViBmBcT5EE8n4EIl" +
        "t7FyZ43S3lRso8ItYyc508+2wYBhWw9l+BvTGsYtNxcWi6z5f5" +
        "Fm+wIDAQAB";

        keyUtil.getPublicKeyFromString(keyString);

        assertNotNull(keyUtil.getPublicKeyFromString(keyString));
        assertTrue(keyUtil.getPublicKeyFromString(keyString) instanceof PublicKey);

        StringUtil stringUtil = new StringUtil();
        assertEquals(keyString, keyUtil.getStringFromPublicKey(keyUtil.getPublicKeyFromString(keyString)));
    }

}
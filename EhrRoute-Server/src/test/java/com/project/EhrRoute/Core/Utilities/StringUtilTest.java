package com.project.EhrRoute.Core.Utilities;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import java.security.KeyPair;
import java.security.PublicKey;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class StringUtilTest
{
    private final StringUtil stringUtil = new StringUtil();
    private final KeyUtil keyUtil = new KeyUtil();
    private final byte[] bytes = "String".getBytes();

    @Test
    public void getStringFromKey() throws Exception
    {
        RsaUtil rsaUtil = new RsaUtil();
        KeyPair keyPair = rsaUtil.rsaGenerateKeyPair();
        PublicKey publicKey = keyPair.getPublic();

        assertNotNull(keyUtil.getStringFromPublicKey(publicKey));
        assertTrue(keyUtil.getStringFromPublicKey(publicKey) instanceof String);
    }

    @Test
    public void getStringFromBytes() throws Exception
    {
        assertNotNull(stringUtil.base64EncodeBytes(bytes));
        assertTrue(stringUtil.base64EncodeBytes(bytes) instanceof String);
    }

    @Test
    public void getUTF8StringFromBytes() throws Exception
    {
        assertNotNull(stringUtil.getUTF8StringFromBytes(bytes));
        assertTrue(stringUtil.getUTF8StringFromBytes(bytes) instanceof String);
        assertEquals(stringUtil.getUTF8StringFromBytes(bytes), "String");
    }
}
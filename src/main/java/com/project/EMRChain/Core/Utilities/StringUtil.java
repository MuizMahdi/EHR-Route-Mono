package com.project.EMRChain.Core.Utilities;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.*;
import java.security.interfaces.ECPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class StringUtil
{
    public static String getStringFromKey(Key key)
    {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static String getStringFromBytes(byte[] input)
    {
        StringBuffer hexString = new StringBuffer();

        for (byte b: input)
        {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append("0");
            hexString.append(hex);
        }

        return hexString.toString();
    }
}

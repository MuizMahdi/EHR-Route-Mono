package com.project.EhrRoute.Core.Utilities;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

@Component
public class StringUtil
{
    public String getStringFromKey(Key key)
    {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public String getStringFromBytes(byte[] bytes)
    {
        StringBuffer hexString = new StringBuffer();

        for (byte b: bytes)
        {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append("0");
            hexString.append(hex);
        }

        return hexString.toString();
    }

    public String getUTF8StringFromBytes(byte[] bytes)
    {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public String base64EncodeBytes(byte[] bytes) { return Base64.getEncoder().encodeToString(bytes); }

    public byte[] base64DecodeString(String string) { return Base64.getDecoder().decode(string); }
}

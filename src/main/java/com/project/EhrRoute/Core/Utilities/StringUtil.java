package com.project.EhrRoute.Core.Utilities;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class StringUtil
{
    // For converting hash bytes to hex string
    public String getStringFromBytes(byte[] bytes) {
        StringBuffer hexString = new StringBuffer();

        for (byte b: bytes)
        {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append("0");
            hexString.append(hex);
        }

        return hexString.toString();
    }

    // For converting bytes of primitive types bytes to String
    public String getUTF8StringFromBytes(byte[] bytes)
    {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    // For base64 encoding of bytes hashes to strings
    public String base64EncodeBytes(byte[] bytes) { return Base64.getEncoder().encodeToString(bytes); }

    // For base64 decoding string hashes to bytes
    public byte[] base64DecodeString(String string) { return Base64.getDecoder().decode(string); }
}

package com.project.EhrRoute.Core.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

@Component
public class KeyUtil
{
    private final Logger logger = LoggerFactory.getLogger(KeyUtil.class);

    public PublicKey getPublicKeyFromString(String stringKey)
    {
        PublicKey publicKey;

        try
        {
            byte[] keyBytes = Base64.getDecoder().decode(stringKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            publicKey = factory.generatePublic(keySpec);
            Arrays.fill(keyBytes, (byte) 0);
            return publicKey;
        }
        catch (GeneralSecurityException Ex) {
            logger.error(Ex.getMessage());
        }

        return null;
    }

    public String getStringFromPublicKey(PublicKey key)
    {
        try {
            KeyFactory factory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = factory.getKeySpec(key, X509EncodedKeySpec.class);
            byte[] keyBytes = keySpec.getEncoded();
            String stringKey = Base64.getEncoder().encodeToString(keyBytes);
            Arrays.fill(keyBytes, (byte) 0);
            return stringKey;
        }
        catch (GeneralSecurityException Ex) {
            logger.error(Ex.getMessage());
        }

        return null;
    }

    public PrivateKey getPrivateKeyFromString(String stringKey) throws GeneralSecurityException
    {
        byte[] keyBytes = Base64.getDecoder().decode(stringKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = factory.generatePrivate(keySpec);
        Arrays.fill(keyBytes, (byte) 0);
        return privateKey;
    }

    public String getStringFromPrivateKey(PrivateKey priv) throws GeneralSecurityException
    {
        KeyFactory fact = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec spec = fact.getKeySpec(priv, PKCS8EncodedKeySpec.class);
        byte[] packed = spec.getEncoded();
        String key64 = Base64.getEncoder().encodeToString(packed);
        Arrays.fill(packed, (byte) 0);
        return key64;
    }
}

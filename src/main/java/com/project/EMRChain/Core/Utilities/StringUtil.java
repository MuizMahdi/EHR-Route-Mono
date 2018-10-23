package com.project.EMRChain.Core.Utilities;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class StringUtil
{
    public static String getStringFromKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static Key getPublicKeyFromString(String stringKey) throws GeneralSecurityException {
        Security.addProvider(new BouncyCastleProvider()); // Set BC as provider
        byte[] keyBytes = Base64.getDecoder().decode(stringKey);
        KeyFactory keyFactory = KeyFactory.getInstance("ECDSA", "BC"); // ECDSA KeyFactory using BC
        PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(keyBytes));
        return publicKey;
    }
}

package com.project.EMRChain.Core.Utilities;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Component;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class ECKeyUtil
{
    public PublicKey getPublicKeyFromString(String stringKey) throws GeneralSecurityException
    {
        Security.addProvider(new BouncyCastleProvider()); // Set BC as provider
        byte[] keyBytes = Base64.getDecoder().decode(stringKey);
        KeyFactory factory = KeyFactory.getInstance("ECDSA","BC");
        PublicKey key = factory.generatePublic(new X509EncodedKeySpec(keyBytes));
        return key;
    }

    public PublicKey getPublicKeyFromBytes(byte[] encoding) throws Exception
    {
        X509EncodedKeySpec ks = new X509EncodedKeySpec(encoding);
        KeyFactory kf = KeyFactory.getInstance("EC");
        return kf.generatePublic(ks);
    }
}
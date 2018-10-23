package com.project.EMRChain.Core.Utilities;
import com.project.EMRChain.Core.Transaction;
import com.project.EMRChain.Utilities.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.*;
import java.security.spec.ECGenParameterSpec;

public class EcdsaUtil
{
    private static Logger logger = LoggerFactory.getLogger(EcdsaUtil.class);

    // Applies ECDSA Signature and returns the result (as bytes)
    public static byte[] EC_Sign(PrivateKey privateKey, Transaction transaction) throws Exception
    {
        Signature digitalSignatureAlgorithm = Signature.getInstance("SHA256withECDSA");

        digitalSignatureAlgorithm.initSign(privateKey);

        String data = StringUtil.getStringFromKey(transaction.getSender()) +
        StringUtil.getStringFromKey(transaction.getRecipient()) +
        JsonUtil.createJson(transaction);

        digitalSignatureAlgorithm.update(data.getBytes());

        byte[] signature = digitalSignatureAlgorithm.sign();

        return signature;
    }

    public static KeyPair EC_GenerateKeyPair() throws Exception
    {
        ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256k1");
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("EC");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

        keyPairGen.initialize(ecSpec, random);

        return keyPairGen.generateKeyPair();
    }

}

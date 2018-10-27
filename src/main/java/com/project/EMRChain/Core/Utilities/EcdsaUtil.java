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

    // Applies ECDSA Signature to a transaction and returns signature.
    public static byte[] ecSign(PrivateKey privateKey, Transaction transaction) throws Exception
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

    // Verifies whether the Transaction belongs to sender or not
    public static boolean ecVerifyTransactionSignature(PublicKey publicKey, Transaction transaction)
    {
        String data = transaction.getTransactionData();

        if (transaction.getSignature() == null) {
            transaction.setSignature("".getBytes());
        }

        byte[] signature = transaction.getSignature();

        try
        {
            Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(data.getBytes());
            return ecdsaVerify.verify(signature);
        }
        catch(GeneralSecurityException exception)
        {
            logger.error(exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    public static KeyPair ecGenerateKeyPair()
    {
        try
        {
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256k1");
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("EC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

            keyPairGen.initialize(ecSpec, random);

            return keyPairGen.generateKeyPair();
        }
        catch (GeneralSecurityException exception)
        {
            logger.error(exception.getMessage());
            throw new RuntimeException(exception);
        }

    }

}

package com.project.EMRChain.Core.Utilities;
import com.project.EMRChain.Core.Transaction;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.security.*;
import java.security.spec.ECGenParameterSpec;

@Component
public class EcdsaUtil
{
    private Logger logger = LoggerFactory.getLogger(EcdsaUtil.class);

    // Applies ECDSA Signature to a transaction and returns signature.
    public byte[] ecSign(PrivateKey privateKey, Transaction transaction) throws Exception
    {
        Signature digitalSignatureAlgorithm = Signature.getInstance("SHA256withECDSA");

        digitalSignatureAlgorithm.initSign(privateKey);

        String data = transaction.getTransactionData();

        digitalSignatureAlgorithm.update(data.getBytes());

        byte[] signature = digitalSignatureAlgorithm.sign();

        return signature;
    }

    // Verifies whether the Transaction belongs to sender or not
    public boolean ecVerifyTransactionSignature(PublicKey publicKey, Transaction transaction)
    {
        String data = transaction.getTransactionData();

        if (transaction.getSignature() == null) {
            transaction.setSignature("".getBytes());
        }

        byte[] signature = transaction.getSignature();

        try
        {
            Security.addProvider(new BouncyCastleProvider());
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

    public KeyPair ecGenerateKeyPair()
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

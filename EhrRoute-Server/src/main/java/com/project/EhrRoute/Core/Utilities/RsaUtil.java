package com.project.EhrRoute.Core.Utilities;
import com.project.EhrRoute.Core.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.security.*;

@Component
public class RsaUtil
{
    private Logger logger = LoggerFactory.getLogger(RsaUtil.class);

    // Applies ECDSA Signature to a transaction and returns signature.
    public byte[] rsaSign(PrivateKey privateKey, Transaction transaction) throws Exception
    {
        Signature digitalSignatureAlgorithm = Signature.getInstance("SHA256withRSA");

        digitalSignatureAlgorithm.initSign(privateKey);

        String data = transaction.getTransactionData();

        digitalSignatureAlgorithm.update(data.getBytes());

        byte[] signature = digitalSignatureAlgorithm.sign();

        return signature;
    }

    // Verifies whether the Transaction belongs to sender or not
    public boolean rsaVerifyTransactionSignature(PublicKey publicKey, Transaction transaction)
    {
        String data = transaction.getTransactionData();

        if (transaction.getSignature() == null) {
            transaction.setSignature("".getBytes());
        }

        byte[] signature = transaction.getSignature();

        try
        {
            //Security.addProvider(new BouncyCastleProvider());
            //Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
            Signature rsaSignature = Signature.getInstance("SHA256withRSA");
            rsaSignature.initVerify(publicKey);
            rsaSignature.update(data.getBytes());
            return rsaSignature.verify(signature);
        }
        catch(GeneralSecurityException exception)
        {
            logger.error(exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    public KeyPair rsaGenerateKeyPair()
    {
        try
        {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            KeyPair keyPair = generator.generateKeyPair();

            return keyPair;
        }
        catch (GeneralSecurityException exception)
        {
            logger.error(exception.getMessage());
            throw new RuntimeException(exception);
        }

    }

}

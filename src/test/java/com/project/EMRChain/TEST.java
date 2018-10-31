package com.project.EMRChain;
import com.project.EMRChain.Core.Utilities.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.security.KeyPair;
import java.security.PublicKey;

public class TEST
{
    /*
    @Autowired
    private EcdsaUtil ecdsaUtil;
    @Autowired
    private StringUtil stringUtil;
    @Autowired
    private Base58 base58;
    @Autowired
    private ECKeyUtil ecKeyUtil;

    private void test02() throws Exception
    {
        KeyPair keyPair = ecdsaUtil.ecGenerateKeyPair();
        PublicKey senderPublicKey = keyPair.getPublic();

        String senderPubKeyString = stringUtil.getStringFromKey(senderPublicKey);
        String senderBase58Key = base58.encode(senderPubKeyString.getBytes());
        System.out.println(senderBase58Key);
        PublicKey senderAddress = ecKeyUtil.getPublicKeyFromString(senderPubKeyString);
        System.out.println(senderAddress);
    }
    */
}
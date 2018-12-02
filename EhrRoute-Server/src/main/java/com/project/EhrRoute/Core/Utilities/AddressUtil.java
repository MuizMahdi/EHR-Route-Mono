package com.project.EhrRoute.Core.Utilities;
import com.project.EhrRoute.Core.Transaction;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.security.PublicKey;

@Component
public class AddressUtil
{
    private StringUtil stringUtil;
    private HashUtil hashUtil;
    private Base58 base58;

    @Autowired
    public AddressUtil(StringUtil stringUtil, HashUtil hashUtil ,Base58 base58) {
        this.stringUtil = stringUtil;
        this.hashUtil = hashUtil;
        this.base58 = base58;
    }


    public String generateAddress(PublicKey publicKey)
    {
        // Perform SHA-256 hashing on the public key
        byte[] hash = hashUtil.SHA256(stringUtil.getStringFromKey(publicKey).getBytes());

        // Perform RIPEMD-160 hashing on the result of SHA-256
        RIPEMD160Digest digest = new RIPEMD160Digest();
        digest.update(hash, 0, hash.length);
        byte[] RIPEMD160Hash = new byte[digest.getDigestSize()];
        digest.doFinal(RIPEMD160Hash, 0);

        // Add version byte in front of RIPEMD-160 hash
        byte[] extendedRIPEMD160Hash = new byte[RIPEMD160Hash.length+1];
        System.arraycopy(RIPEMD160Hash, 0, extendedRIPEMD160Hash, 1, RIPEMD160Hash.length);

        // Perform Base58Check
        String address = base58.encodeChecked(1, extendedRIPEMD160Hash);

        return address;
    }

    public boolean confirmTransactionSenderAddress(Transaction transaction)
    {
        String senderAddress = transaction.getSenderAddress().getAddress();
        PublicKey senderPubKey = transaction.getSenderPubKey();

        if (senderAddress == null || senderAddress.isEmpty() ||
            senderPubKey == null || !senderAddress.equals(generateAddress(senderPubKey))
        ) {
            return false;
        }

        return true;
    }
}

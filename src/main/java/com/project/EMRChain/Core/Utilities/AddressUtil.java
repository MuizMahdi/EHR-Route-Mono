package com.project.EMRChain.Core.Utilities;
import com.project.EMRChain.Core.Address;
import com.project.EMRChain.Core.Transaction;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import java.security.PublicKey;

public class AddressUtil
{
    public static String generateAddress(PublicKey publicKey)
    {
        // Perform SHA-256 hashing on the public key
        byte[] hash = HashUtil.SHA256(StringUtil.getStringFromKey(publicKey).getBytes());

        // Perform RIPEMD-160 hashing on the result of SHA-256
        RIPEMD160Digest digest = new RIPEMD160Digest();
        digest.update(hash, 0, hash.length);
        byte[] RIPEMD160Hash = new byte[digest.getDigestSize()];
        digest.doFinal(RIPEMD160Hash, 0);

        // Add version byte in front of RIPEMD-160 hash
        byte[] extendedRIPEMD160Hash = new byte[RIPEMD160Hash.length+1];
        System.arraycopy(RIPEMD160Hash, 0, extendedRIPEMD160Hash, 1, RIPEMD160Hash.length);

        // Perform Base58Check
        String address = Base58.encodeChecked(1, extendedRIPEMD160Hash);

        return address;
    }

    public static boolean confirmTransactionSenderAddress(Transaction transaction)
    {
        String senderAddress = transaction.getSenderAddress().getAddress();
        PublicKey senderPubKey = transaction.getSenderPubKey();

        if (senderAddress == null || senderAddress.isEmpty() || senderPubKey == null)
        {
            return false;
        }

        if (!senderAddress.equals(AddressUtil.generateAddress(senderPubKey)))
        {
            return false;
        }

        return true;
    }
}
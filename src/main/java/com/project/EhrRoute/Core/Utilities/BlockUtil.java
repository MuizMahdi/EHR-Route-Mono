package com.project.EhrRoute.Core.Utilities;
import com.project.EhrRoute.Core.Block;
import com.project.EhrRoute.Core.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.security.PrivateKey;


@Component
public class BlockUtil
{
    private KeyUtil keyUtil;
    private RsaUtil rsaUtil;
    private HashUtil hashUtil;

    @Autowired
    public BlockUtil(KeyUtil keyUtil, RsaUtil rsaUtil, HashUtil hashUtil) {
        this.keyUtil = keyUtil;
        this.rsaUtil = rsaUtil;
        this.hashUtil = hashUtil;
    }

    public Block signBlock(String encodedPrivateKey, Block block) throws Exception {
        // Get Transaction from Block
        Transaction transaction = block.getTransaction();

        // Convert Base64 encoded String privateKey to PrivateKey
        PrivateKey privateKey = keyUtil.getPrivateKeyFromString(encodedPrivateKey);

        // Sign transaction and get signature
        byte[] signature = rsaUtil.rsaSign(privateKey, transaction);

        // Update the transaction signature
        transaction.setSignature(signature);

        // Update the block's transaction with the signed transaction
        block.setTransaction(transaction);

        return block;
    }

    public Block updateBlockLeafHash(Block block) {
        // Update the TxID
        block.getTransaction().setTransactionId(
                hashUtil.hashTransactionData(block.getTransaction())
        );

        // Update the merkle leaf hash
        block.getBlockHeader().setMerkleLeafHash(
                hashUtil.SHA256(block.getTransaction().getTransactionId())
        );

        return block;
    }
}

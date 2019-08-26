package com.project.EhrRoute.Core;
import com.project.EhrRoute.Core.Utilities.HashUtil;

public class Block
{
    private BlockHeader blockHeader;
    private Transaction transaction;

    public Block() { }
    public Block(Transaction transaction, BlockHeader blockHeader) {
        this.transaction = transaction;
        this.blockHeader = blockHeader;
    }

    public void initBlock() {
        HashUtil hashUtil = new HashUtil();
        blockHeader.setMerkleLeafHash(hashUtil.SHA256(transaction.getTransactionId()));
    }

    public BlockHeader getBlockHeader() {
        return blockHeader;
    }
    public Transaction getTransaction() {
        return transaction;
    }
    public void setBlockHeader(BlockHeader blockHeader) {
        this.blockHeader = blockHeader;
    }
    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public String toString(){
        return blockHeader.toString();
    }
}

package com.project.EMRChain.Core;

public class Block
{
    private BlockHeader blockHeader;
    private Transaction transaction;

    public Block() { }
    public Block(Transaction transaction, BlockHeader blockHeader) {
        this.transaction = transaction;
        this.blockHeader = blockHeader;
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
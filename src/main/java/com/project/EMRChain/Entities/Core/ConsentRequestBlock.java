package com.project.EMRChain.Entities.Core;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "ConsentRequestBlock")
public class ConsentRequestBlock
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank private String hash;
    @NotBlank private String previousHash;
    @NotBlank private Long timeStamp;
    @NotBlank private Long index;
    @NotBlank private String merkleRoot;
    @NotBlank private String transactionId;
    @NotBlank private String senderPubKey;
    @NotBlank private String senderAddress;
    @NotBlank private String recipientAddress;
    // Transaction signature is blank when saved on patient consent requests because the patient is the one that signs it
    private String signature;

    public ConsentRequestBlock() { }
    public ConsentRequestBlock(@NotBlank String hash, @NotBlank String previousHash, @NotBlank Long timeStamp, @NotBlank Long index, @NotBlank String merkleRoot, @NotBlank String transactionId, @NotBlank String senderPubKey, @NotBlank String senderAddress, @NotBlank String recipientAddress, @NotBlank String signature) {
        this.hash = hash;
        this.previousHash = previousHash;
        this.timeStamp = timeStamp;
        this.index = index;
        this.merkleRoot = merkleRoot;
        this.transactionId = transactionId;
        this.senderPubKey = senderPubKey;
        this.senderAddress = senderAddress;
        this.recipientAddress = recipientAddress;
        this.signature = signature;
    }

    public Long getId() {
        return id;
    }
    public String getHash() {
        return hash;
    }
    public Long getIndex() {
        return index;
    }
    public Long getTimeStamp() {
        return timeStamp;
    }
    public String getSignature() {
        return signature;
    }
    public String getMerkleRoot() {
        return merkleRoot;
    }
    public String getPreviousHash() {
        return previousHash;
    }
    public String getSenderPubKey() {
        return senderPubKey;
    }
    public String getTransactionId() {
        return transactionId;
    }
    public String getSenderAddress() {
        return senderAddress;
    }
    public String getRecipientAddress() {
        return recipientAddress;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setHash(String hash) {
        this.hash = hash;
    }
    public void setIndex(Long index) {
        this.index = index;
    }
    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }
    public void setSignature(String signature) {
        this.signature = signature;
    }
    public void setMerkleRoot(String merkleRoot) {
        this.merkleRoot = merkleRoot;
    }
    public void setSenderPubKey(String senderPubKey) {
        this.senderPubKey = senderPubKey;
    }
    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }
    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }
}

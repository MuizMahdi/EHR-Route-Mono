package com.project.EhrRoute.Entities.Core;
import com.project.EhrRoute.Entities.EHR.EhrHistory;
import com.project.EhrRoute.Entities.EHR.EhrProblems;
import com.project.EhrRoute.Entities.EHR.EhrAllergies;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;

import java.util.Set;
import java.util.HashSet;


@Entity
@Table(name = "ConsentRequestBlock")
public class ConsentRequestBlock
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull @NotBlank private String hash;
    @NotNull @NotBlank private String previousHash;
    @NotNull @NotBlank private String merkleRoot;
    @NotNull @NotBlank private String transactionId;
    @NotNull @NotBlank private String senderPubKey;
    @NotNull @NotBlank private String senderAddress;
    @NotNull @NotBlank private String recipientAddress;
    @NotNull @NotBlank private String networkUUID;
    @NotNull @NotBlank private String providerUUID;
    @NotNull @NotBlank private String chainRootWithBlock;
    @NotNull private Long userID;
    @NotNull private Long timeStamp;
    @NotNull private Long blockIndex;


    // Transaction signature is blank when saved on patient consent requests because the patient is the one that signs it
    private String signature;

    @OneToMany(mappedBy = "consentRequestBlock", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval=true)
    private Set<EhrProblems> problems = new HashSet<>();

    @OneToMany(mappedBy = "consentRequestBlock", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval=true)
    private Set<EhrAllergies> allergies = new HashSet<>();

    @OneToMany(mappedBy = "consentRequestBlock", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval=true)
    private Set<EhrHistory> history = new HashSet<>();


    public ConsentRequestBlock() { }
    public ConsentRequestBlock(@NotBlank String hash, @NotBlank String previousHash, @NotBlank Long timeStamp, @NotBlank Long blockIndex, @NotBlank String merkleRoot, @NotBlank String transactionId, @NotBlank String senderPubKey, @NotBlank String senderAddress, @NotBlank String recipientAddress, String networkUUID, String signature, @NotBlank Long userID, @NotBlank String providerUUID, @NotBlank String chainRootWithBlock) {
        this.hash = hash;
        this.previousHash = previousHash;
        this.timeStamp = timeStamp;
        this.blockIndex = blockIndex;
        this.merkleRoot = merkleRoot;
        this.transactionId = transactionId;
        this.senderPubKey = senderPubKey;
        this.senderAddress = senderAddress;
        this.recipientAddress = recipientAddress;
        this.networkUUID = networkUUID;
        this.signature = signature;
        this.userID = userID;
        this.providerUUID = providerUUID;
        this.chainRootWithBlock = chainRootWithBlock;
    }


    public void addProblem(EhrProblems problem) {
        problems.add(problem);
    }
    public void removeProblem(EhrProblems problem) {
        problems.remove(problem);
    }

    public void addAllergy(EhrAllergies allergy) {
        allergies.add(allergy);
    }
    public void removeAllergy(EhrAllergies allergy) {
        allergies.remove(allergy);
    }

    public void addHistory(EhrHistory historicalCondition) {
        history.add(historicalCondition);
    }
    public void removeHistory(EhrHistory historicalCondition) {
        history.remove(historicalCondition);
    }


    public Long getId() {
        return id;
    }
    public String getHash() {
        return hash;
    }
    public Long getUserID() {
        return userID;
    }
    public Long getTimeStamp() {
        return timeStamp;
    }
    public String getSignature() {
        return signature;
    }
    public Long getBlockIndex() {
        return blockIndex;
    }
    public String getMerkleRoot() {
        return merkleRoot;
    }
    public String getNetworkUUID() {
        return networkUUID;
    }
    public Set<EhrHistory> getHistory() {
        return history;
    }
    public String getProviderUUID() {
        return providerUUID;
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
    public Set<EhrProblems> getProblems() {
        return problems;
    }
    public Set<EhrAllergies> getAllergies() {
        return allergies;
    }
    public String getRecipientAddress() {
        return recipientAddress;
    }
    public String getChainRootWithBlock() {
        return chainRootWithBlock;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setHash(String hash) {
        this.hash = hash;
    }
    public void setUserID(Long userID) {
        this.userID = userID;
    }
    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }
    public void setSignature(String signature) {
        this.signature = signature;
    }
    public void setHistory(Set<EhrHistory> history) {
        this.history = history;
    }
    public void setBlockIndex(Long blockIndex) {
        this.blockIndex = blockIndex;
    }
    public void setMerkleRoot(String merkleRoot) {
        this.merkleRoot = merkleRoot;
    }
    public void setProblems(Set<EhrProblems> problems) {
        this.problems = problems;
    }
    public void setNetworkUUID(String networkUUID) {
        this.networkUUID = networkUUID;
    }
    public void setAllergies(Set<EhrAllergies> allergies) {
        this.allergies = allergies;
    }
    public void setProviderUUID(String providerUUID) {
        this.providerUUID = providerUUID;
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
    public void setChainRootWithBlock(String chainRootWithBlock) {
        this.chainRootWithBlock = chainRootWithBlock;
    }
}
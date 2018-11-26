package com.project.EMRChain.Entities.Core;
import com.project.EMRChain.Entities.EHR.EhrAllergies;
import com.project.EMRChain.Entities.EHR.EhrHistory;
import com.project.EMRChain.Entities.EHR.EhrProblems;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ConsentRequestBlock")
public class ConsentRequestBlock
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull @NotBlank private String hash;
    @NotNull @NotBlank private String previousHash;
    @NotNull @NotBlank private Long timeStamp;
    @NotNull @NotBlank private Long blockIndex;
    @NotNull @NotBlank private String merkleRoot;
    @NotNull @NotBlank private String transactionId;
    @NotNull @NotBlank private String senderPubKey;
    @NotNull @NotBlank private String senderAddress;
    @NotNull @NotBlank private String recipientAddress;
    @NotNull @NotBlank private Long userID;

    // Transaction signature is blank when saved on patient consent requests because the patient is the one that signs it
    private String signature;

    @OneToMany(mappedBy = "consentRequestBlock", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval=true)
    private Set<EhrProblems> problems = new HashSet<>();

    @OneToMany(mappedBy = "consentRequestBlock", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval=true)
    private Set<EhrAllergies> allergies = new HashSet<>();

    @OneToMany(mappedBy = "consentRequestBlock", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval=true)
    private Set<EhrHistory> history = new HashSet<>();


    public ConsentRequestBlock() { }
    public ConsentRequestBlock(@NotBlank String hash, @NotBlank String previousHash, @NotBlank Long timeStamp, @NotBlank Long blockIndex, @NotBlank String merkleRoot, @NotBlank String transactionId, @NotBlank String senderPubKey, @NotBlank String senderAddress, @NotBlank String recipientAddress, String signature, @NotBlank Long userID) {
        this.hash = hash;
        this.previousHash = previousHash;
        this.timeStamp = timeStamp;
        this.blockIndex = blockIndex;
        this.merkleRoot = merkleRoot;
        this.transactionId = transactionId;
        this.senderPubKey = senderPubKey;
        this.senderAddress = senderAddress;
        this.recipientAddress = recipientAddress;
        this.signature = signature;
        this.userID = userID;
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
    public Set<EhrHistory> getHistory() {
        return history;
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
    public void setAllergies(Set<EhrAllergies> allergies) {
        this.allergies = allergies;
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
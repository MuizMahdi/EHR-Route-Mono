package com.project.EhrRoute.Payload.Core;


import com.project.EhrRoute.Payload.EHR.TempEhrHistory;

import java.util.List;

public class UserConsentResponse
{
    private SerializableBlock block;
    private List<TempEhrHistory> medicalHistory;
    private String userPrivateKey;
    private String userAddress;
    private String consentRequestUUID;
    private String providerUUID;
    private String networkUUID;
    private Long userID;


    public UserConsentResponse() { }
    public UserConsentResponse(SerializableBlock block, List<TempEhrHistory> medicalHistory, String userPrivateKey, String userAddress, String consentRequestUUID, String providerUUID, String networkUUID, Long userID) {
        this.block = block;
        this.medicalHistory = medicalHistory;
        this.userPrivateKey = userPrivateKey;
        this.userAddress = userAddress;
        this.consentRequestUUID = consentRequestUUID;
        this.providerUUID = providerUUID;
        this.networkUUID = networkUUID;
        this.userID = userID;
    }


    public Long getUserID() {
        return userID;
    }
    public SerializableBlock getBlock() {
        return block;
    }
    public String getNetworkUUID() {
        return networkUUID;
    }
    public String getUserAddress() {
        return userAddress;
    }
    public String getProviderUUID() {
        return providerUUID;
    }
    public String getUserPrivateKey() {
        return userPrivateKey;
    }
    public String getConsentRequestUUID() {
        return consentRequestUUID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }
    public void setBlock(SerializableBlock block) {
        this.block = block;
    }
    public void setNetworkUUID(String networkUUID) {
        this.networkUUID = networkUUID;
    }
    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }
    public void setProviderUUID(String providerUUID) {
        this.providerUUID = providerUUID;
    }
    public void setUserPrivateKey(String userPrivateKey) {
        this.userPrivateKey = userPrivateKey;
    }
    public void setConsentRequestUUID(String consentRequestUUID) {
        this.consentRequestUUID = consentRequestUUID;
    }

    public List<TempEhrHistory> getMedicalHistory() {
        return medicalHistory;
    }
    public void setMedicalHistory(List<TempEhrHistory> medicalHistory) {
        this.medicalHistory = medicalHistory;
    }
}

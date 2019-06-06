package com.project.EhrRoute.Payload.Core;

public class BlockMetadata
{
    // The UUID of the consent request that resulted in this block's generation and broadcasting
    private String consentRequestUUID;

    public BlockMetadata() { }
    public BlockMetadata(String consentRequestUUID) {
        this.consentRequestUUID = consentRequestUUID;
    }

    public String getConsentRequestUUID() {
        return consentRequestUUID;
    }
    public void setConsentRequestUUID(String consentRequestUUID) {
        this.consentRequestUUID = consentRequestUUID;
    }
}

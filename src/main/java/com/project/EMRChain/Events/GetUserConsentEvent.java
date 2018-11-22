package com.project.EMRChain.Events;
import com.project.EMRChain.Payload.Core.SerializableBlock;
import org.springframework.context.ApplicationEvent;

public class GetUserConsentEvent extends ApplicationEvent
{
    private SerializableBlock block;
    private String providerUUID;
    private String userID;

    public GetUserConsentEvent(SerializableBlock block, String providerUUID, String userID) {
        super(block);
        this.providerUUID = providerUUID;
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }
    public SerializableBlock getBlock() {
        return block;
    }
    public String getProviderUUID() {
        return providerUUID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }
    public void setBlock(SerializableBlock block) {
        this.block = block;
    }
    public void setProviderUUID(String providerUUID) {
        this.providerUUID = providerUUID;
    }
}

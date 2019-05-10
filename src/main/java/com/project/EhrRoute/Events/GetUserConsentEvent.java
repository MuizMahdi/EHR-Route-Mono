package com.project.EhrRoute.Events;
import com.project.EhrRoute.Payload.Core.SerializableBlock;
import org.springframework.context.ApplicationEvent;

public class GetUserConsentEvent extends ApplicationEvent
{
    private SerializableBlock block;
    private String providerUUID;
    private String networkUUID;
    private String userID;

    public GetUserConsentEvent(Object source, SerializableBlock block, String providerUUID, String networkUUID,String userID) {
        super(source);
        this.block = block;
        this.providerUUID = providerUUID;
        this.networkUUID = networkUUID;
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }
    public SerializableBlock getBlock() {
        return block;
    }
    public String getNetworkUUID() {
        return networkUUID;
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
    public void setNetworkUUID(String networkUUID) {
        this.networkUUID = networkUUID;
    }
    public void setProviderUUID(String providerUUID) {
        this.providerUUID = providerUUID;
    }
}
